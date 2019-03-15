package com.luxfacta.treinamento.desafioandroidparte4.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luxfacta.treinamento.desafioandroidparte4.Application.MyApplication;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;
import com.luxfacta.treinamento.desafioandroidparte4.R;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class EditarActivity extends AppCompatActivity implements Serializable {

    private EditText campoDescricao;
    private EditText campoTipo;
    private EditText campoValor;
    private ImageView imagem;
    private Uri photo;
    private Despesas despesas;
    private Despesas recuperado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        campoDescricao = findViewById(R.id.campoDescricao);
        campoTipo = findViewById(R.id.campoTipo);
        campoValor = findViewById(R.id.campoValor);
        imagem = findViewById(R.id.image_cadastro_id);


        Intent intent = getIntent();
        recuperado = (Despesas) intent.getSerializableExtra("expense");

        campoDescricao.setText(recuperado.getDescricao());
        campoTipo.setText(recuperado.getTipo());
        campoValor.setText(String.valueOf(recuperado.getValor()));
        photo = Uri.parse(recuperado.getImagem());

        if (recuperado.getImagem() != null) {
            Glide.with(this).load(recuperado.getImagem()).into(imagem);
        }


        findViewById(R.id.image_cadastro_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
                if (!hasPermissions(EditarActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditarActivity.this, PERMISSIONS, 4);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
                    builder = AlertaCamera(builder);
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }

            }
        });

        findViewById(R.id.btCadastrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateDados();
                Intent intent = new Intent(EditarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    public void updateDados() {

        String valor = campoValor.getText().toString();
        String descricao = campoDescricao.getText().toString().trim();


        if (descricao.isEmpty()) {
            campoDescricao.setError("Insira uma descricao!");
            campoDescricao.requestFocus();
            return;

        }

        if (valor.isEmpty()) {
            campoValor.setError("Insira um valor!");
            campoValor.requestFocus();
            return;
        }


        if (photo == null) {
            photo = Uri.parse("Default");
        }

        despesas = Despesas.getInstance();
        despesas.setId(recuperado.getId());
        despesas.setDescricao(campoDescricao.getText().toString());
        despesas.setValor(Double.parseDouble(valor));
        despesas.setTipo(campoTipo.getText().toString());
        despesas.setImagem(photo.toString());


        if (((MyApplication) EditarActivity.this.getApplication()).updateExpense(despesas)) {
            campoDescricao.setText("");
            campoTipo.setText("");
            campoValor.setText("");
            Drawable drawable = ContextCompat.getDrawable(EditarActivity.this, R.mipmap.camera);
            imagem.setImageDrawable(drawable);
            Toast.makeText(EditarActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(EditarActivity.this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
        return;

    }

    private AlertDialog.Builder AlertaCamera(AlertDialog.Builder builder) {

        builder.setTitle("Adicione uma foto para a despesa");
        builder.setMessage("Escolha uma opção:");
        builder.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                showImageToCamera();
            }
        });
        builder.setNegativeButton("GALERIA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                showImageToGalery();
            }
        });
        return builder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case 0:
                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    imagem.setScaleType(ImageView.ScaleType.FIT_XY);
                    imagem.setImageBitmap(foto);

                    String aux = getImageUri(EditarActivity.this, foto);
                    photo = Uri.parse(aux);

                    Glide.with(EditarActivity.this).load(photo).into(imagem);
                    break;
                case 1:
                    photo = data.getData();
                    Glide.with(EditarActivity.this).load(photo).into(imagem);
                    break;
                case 3:
                    Toast.makeText(EditarActivity.this, "Selecione a foto novamente!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 4) {
            Toast.makeText(EditarActivity.this, "Permissao concedida, escolha novamente a imagem!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", "");
        return path;
    }

    private void showImageToCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void showImageToGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent.createChooser(intent, getString(R.string.toast_select_photo)), 1);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
 }




