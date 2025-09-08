package trabalho;

import java.io.*;
import java.util.*;

class Tarefa {
    String nome;
    int tempo;
    LinkedList<Tarefa> filhos = new LinkedList<>();
    int pendencias = 0;

    Tarefa(String nome, int tempo) {
        this.nome = nome;
        this.tempo = tempo;
    }
}

public class App {
    HashMap<String, Tarefa> tarefas = new HashMap<>();
    int processadores;

    private Tarefa pegarOuCriar(String nome) {
        if (!tarefas.containsKey(nome)) {
            String[] partes = nome.split("_");
            int t = Integer.parseInt(partes[1]);
            tarefas.put(nome, new Tarefa(nome, t));
        }
        return tarefas.get(nome);
    }

    public void carregar(String caminho) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String primeira = br.readLine(); // "# Proc N"
        processadores = Integer.parseInt(primeira.split(" ")[2]);

        String linha;
        while ((linha = br.readLine()) != null) {
            if (linha.contains("->")) {
                String[] p = linha.split("->");
                Tarefa a = pegarOuCriar(p[0].trim());
                Tarefa b = pegarOuCriar(p[1].trim());
                a.filhos.add(b);
                b.pendencias++;
            }
        }
        br.close();
    }

    public int simular(String politica) {
        LinkedList<Tarefa> prontas = new LinkedList<>();
        for (Tarefa t : tarefas.values()) if (t.pendencias == 0) prontas.add(t);

        int tempo = 0;
        LinkedList<int[]> exec = new LinkedList<>();

        while (!prontas.isEmpty() || !exec.isEmpty()) {
            while (exec.size() < processadores && !prontas.isEmpty()) {
                Tarefa escolhida = escolher(prontas, politica);
                prontas.remove(escolhida);
                exec.add(new int[]{tempo + escolhida.tempo, escolhida.tempo});

                for (Tarefa f : escolhida.filhos) {
                    f.pendencias--;
                    if (f.pendencias == 0) prontas.add(f);
                }
            }

            // pega o menor tempo de término
            int menor = exec.get(0)[0];
            int idx = 0;
            for (int i = 1; i < exec.size(); i++) {
                if (exec.get(i)[0] < menor) {
                    menor = exec.get(i)[0];
                    idx = i;
                }
            }
            tempo = menor;
            exec.remove(idx);
        }
        return tempo;
    }

    private Tarefa escolher(LinkedList<Tarefa> lista, String politica) {
        Tarefa esc = lista.getFirst();
        for (Tarefa t : lista) {
            if (politica.equals("MIN") && t.tempo < esc.tempo) esc = t;
            if (politica.equals("MAX") && t.tempo > esc.tempo) esc = t;
        }
        return esc;
    }

    public static void main(String[] args) throws Exception {
        File pasta = new File("CasosTeste");
        File[] arquivos = pasta.listFiles((d, n) -> n.endsWith(".txt"));
        if (arquivos == null) return;

        for (File arq : arquivos) {
            App app = new App();
            app.carregar(arq.getPath());
            int min = app.simular("MIN");
            int max = app.simular("MAX");
            System.out.println(min + " " + max);
        }
    }
}
