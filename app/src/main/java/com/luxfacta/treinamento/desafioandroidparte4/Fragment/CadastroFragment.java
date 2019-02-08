package com.luxfacta.treinamento.desafioandroidparte4.Fragment;


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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luxfacta.treinamento.desafioandroidparte4.Application.MyApplication;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;
import com.luxfacta.treinamento.desafioandroidparte4.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CadastroFragment extends Fragment {

    private EditText campoDescricao;
    private EditText campoTipo;
    private EditText campoValor;
    private Despesas despesas;
    private ImageView imagem;
    private Uri photo;
    private INotifyInsert mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(!(context instanceof INotifyInsert)){
            throw new RuntimeException("Implemente a notificacao de insercao!");
        }
        mListener = (INotifyInsert) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cdastro, container, false);

        campoDescricao = view.findViewById(R.id.campoDescricao);
        campoTipo = view.findViewById(R.id.campoTipo);
        campoValor = view.findViewById(R.id.campoValor);
        imagem = view.findViewById(R.id.image_cadastro_id);


        view.findViewById(R.id.image_cadastro_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
                if (!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 4);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder = AlertaCamera(builder);
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }

            }
        });


        view.findViewById(R.id.btCadastrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();

                if(mListener != null && despesas != null){
                    List<Despesas> auxList = new ArrayList<>();
                    auxList.addAll(((MyApplication) getActivity().getApplication()).getAllExpenses());
                    Despesas auxDespesas = auxList.get(auxList.size()-1);
                    mListener.notifyInsert(auxDespesas);
                }

            }
        });

        return view;
    }

      public void salvarDados(){

        String valor = campoValor.getText().toString();
        String descricao = campoDescricao.getText().toString().trim();


        if(descricao.isEmpty()){
            campoDescricao.setError("Insira uma descricao!");
            campoDescricao.requestFocus();
            return;

        }

        if(valor.isEmpty()){
            campoValor.setError("Insira um valor!");
            campoValor.requestFocus();
            return;
        }


        if(photo == null){
            photo = Uri.parse("Default");
        }

        despesas = Despesas.getInstance();
        despesas.setDescricao(campoDescricao.getText().toString());
        despesas.setValor(Double.parseDouble(valor));
        despesas.setTipo(campoTipo.getText().toString());
        despesas.setImagem(photo.toString());


        if(((MyApplication) getActivity().getApplication()).insertDespesas(despesas)){
            campoDescricao.setText("");
            campoTipo.setText("");
            campoValor.setText("");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.images);
            imagem.setImageDrawable(drawable);
            Toast.makeText(getContext(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
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

        if(resultCode == Activity.RESULT_OK)
        {

            switch (requestCode)
            {
                case 0:
                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    imagem.setScaleType(ImageView.ScaleType.FIT_XY);
                    imagem.setImageBitmap(foto);

                    String aux = getImageUri(getContext(), foto);
                    photo = Uri.parse(aux);

                    Glide.with(getContext()).load(photo).into(imagem);
                    break;
                case 1:
                    photo = data.getData();
                    Glide.with(getContext()).load(photo).into(imagem);
                    break;
                case 3:
                    Toast.makeText(getContext(), "Selecione a foto novamente!", Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if(requestCode == 4){
            Toast.makeText(getContext(), "Permissao concedida, escolha novamente a imagem!", Toast.LENGTH_SHORT).show();
        }
    }
    private String getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", "");
        return path;
    }
    private void showImageToCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    private void showImageToGalery(){
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



