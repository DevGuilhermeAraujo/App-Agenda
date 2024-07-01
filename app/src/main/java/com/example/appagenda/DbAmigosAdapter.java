package com.example.appagenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DbAmigosAdapter extends RecyclerView.Adapter<DbAmigosHolder> {

    private final List<DbAmigo> amigos;

    public DbAmigosAdapter(List<DbAmigo> amigos) {
        this.amigos = amigos;
    }
    // Este método retorna o layout criado pela ViewHolder, inflado numa view

    @Override
    public DbAmigosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DbAmigosHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_dados_amigo, parent, false));
    }

    // Recebe a ViewHolder e a posição da lista, de forma que um objeto da lista é recuperado pela posição e associado a ela - é o foco da ação para acontecer o processo

    @Override
    public void onBindViewHolder(DbAmigosHolder holder, int position) {
        holder.nmAmigo.setText(amigos.get(position).getNome());
        holder.vlCelular.setText(amigos.get(position).getCelular());

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Activity activity = getActivity(v);
                    Intent intent = new Intent(activity, MainActivity.class); // ou outra Activity que você deseja abrir
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("amigo", amigos.get(adapterPosition));
                    activity.startActivity(intent);
                }
            }
        });

        final DbAmigo amigo = amigos.get(position);
        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    DbAmigo amigo = amigos.get(adapterPosition);
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Confirmação")
                            .setMessage("Tem certeza que deseja excluir o amigo [" + amigo.getNome() + "]?")
                            .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DbAmigosDAO dao = new DbAmigosDAO(holder.itemView.getContext());
                                    boolean sucesso = dao.excluir(amigo.getId());
                                    if (sucesso) {
                                        Snackbar.make(holder.itemView, "Excluindo o amigo [" + amigo.getNome() + "]!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        excluirAmigo(amigo);
                                    } else {
                                        Snackbar.make(holder.itemView, "Erro ao excluir o amigo [" + amigo.getNome() + "]!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create()
                            .show();
                }
            }
        });

    }

    // Esta função retorna a quantidade de itens que há na lista. É importante verificar se a lista possui elementos, para não causar um erro de exceção.

    @Override
    public int getItemCount() {
        return amigos != null ? amigos.size() : 0;
    }

    public void inserirAmigo(DbAmigo amigo){
        amigos.add(amigo);
        notifyItemInserted(getItemCount());
    }

    public void atualizarAmigo(DbAmigo amigo){
        amigos.set(amigos.indexOf(amigo), amigo);
        notifyItemChanged(amigos.indexOf(amigo));
    }


    private Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void excluirAmigo(DbAmigo amigo) {
        int position = amigos.indexOf(amigo);
        if (position != -1) {
            amigos.remove(position);
            notifyItemRemoved(position);
        }
    }



}