package trabalho;

import java.io.*;
import java.util.*;

public class Escalonador {
    Map<String, Tarefa> tarefas = new HashMap<>();
    int processadores = 1;

    private Tarefa criarOuPegar(String nome) {
        if (!tarefas.containsKey(nome)) {
            String[] partes = nome.split("_");
            int t = Integer.parseInt(partes[1]);
            tarefas.put(nome, new Tarefa(nome, t));
        }
        return tarefas.get(nome);
    }

    public void carregar(String caminho) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String primeira = br.readLine();
        processadores = Integer.parseInt(primeira.split(" ")[2]);

        String linha;
        while ((linha = br.readLine()) != null) {
            if (linha.contains("->")) {
                String[] p = linha.split("->");
                String de = p[0].trim();
                String para = p[1].trim();

                Tarefa a = criarOuPegar(de);
                Tarefa b = criarOuPegar(para);
                a.filhos.add(b);
                b.pendencias++;
            }
        }
        br.close();
    }

    public int simular(String politica) {
        // lista de prontas
        List<Tarefa> prontas = new LinkedList<>();
        for (Tarefa t : tarefas.values()) {
            if (t.pendencias == 0) prontas.add(t);
        }

        int tempo = 0;
        // lista de execuções [tempoFim, tarefa]
        List<int[]> exec = new ArrayList<>();

        while (!prontas.isEmpty() || !exec.isEmpty()) {
            // escalar enquanto houver processador
            while (exec.size() < processadores && !prontas.isEmpty()) {
                Tarefa escolhida = escolher(prontas, politica);
                prontas.remove(escolhida);
                exec.add(new int[]{tempo + escolhida.tempo, escolhida.hashCode()});

                for (Tarefa f : escolhida.filhos) {
                    f.pendencias--;
                    if (f.pendencias == 0) prontas.add(f);
                }
            }

            // avança pro menor fim
            int menor = Integer.MAX_VALUE;
            int idx = -1;
            for (int i = 0; i < exec.size(); i++) {
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

    private Tarefa escolher(List<Tarefa> lista, String politica) {
        Tarefa escolhido = lista.get(0);
        for (Tarefa t : lista) {
            if (politica.equals("MIN")) {
                if (t.tempo < escolhido.tempo) escolhido = t;
            } else {
                if (t.tempo > escolhido.tempo) escolhido = t;
            }
        }
        return escolhido;
    }
}
