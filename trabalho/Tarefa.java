package trabalho;

import java.util.LinkedList;
import java.util.List;

public class Tarefa {
    String nome;
    int tempo;
    List<Tarefa> filhos;
    int pendencias;

    public Tarefa(String nome, int tempo) {
        this.nome = nome;
        this.tempo = tempo;
        this.filhos = new LinkedList<>();
        this.pendencias = 0;
    }
}
