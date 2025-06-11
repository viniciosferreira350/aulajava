import java.io.*;
import java.util.*;

public class CadastroClientesMenu {

    public static class Cliente {
        char ativo;
        int codCliente;
        String nomeCliente;
        float vlrCompra;
        int anoPrimeiraCompra;
        boolean emDia;
    }

    static final String NOME_ARQUIVO = "CLIENTES.DAT";

    public static void main(String[] args) {
        Scanner leia = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\nMENU DE CLIENTES");
            System.out.println("1 - Incluir Cliente");
            System.out.println("2 - Consultar Cliente");
            System.out.println("3 - Alterar Cliente");
            System.out.println("4 - Excluir Cliente");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opcao: ");
            opcao = leia.nextInt();

            switch (opcao) {
                case 1: incluirCliente(); break;
                case 2: consultarCliente(); break;
                case 3: alterarCliente(); break;
                case 4: excluirCliente(); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);

        leia.close();
    }

    public static void incluirCliente() {
        try (RandomAccessFile arq = new RandomAccessFile(NOME_ARQUIVO, "rw"); Scanner leia = new Scanner(System.in)) {
            Cliente c = new Cliente();
            System.out.print("Codigo do cliente: ");
            c.codCliente = leia.nextInt(); leia.nextLine();

            // Verifica duplicidade
            if (buscarClientePorCodigo(arq, c.codCliente) != -1) {
                System.out.println("Cliente ja cadastrado!");
                return;
            }

            do {
                System.out.print("Nome (min. 10 caracteres): ");
                c.nomeCliente = leia.nextLine();
            } while (c.nomeCliente.length() < 10);

            do {
                System.out.print("Valor da compra (> 0): ");
                c.vlrCompra = leia.nextFloat();
            } while (c.vlrCompra <= 0);

            do {
                System.out.print("Ano da primeira compra (<= 2013): ");
                c.anoPrimeiraCompra = leia.nextInt();
            } while (c.anoPrimeiraCompra > 2013);

            System.out.print("Cliente em dia (S/N): ");
            char emDiaChar = leia.next().toUpperCase().charAt(0);
            c.emDia = (emDiaChar == 'S');
            c.ativo = 'S';

            arq.seek(arq.length());
            gravarCliente(arq, c);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void consultarCliente() {
        try (RandomAccessFile arq = new RandomAccessFile(NOME_ARQUIVO, "r"); Scanner leia = new Scanner(System.in)) {
            System.out.print("Informe o codigo do cliente: ");
            int cod = leia.nextInt();
            long pos = buscarClientePorCodigo(arq, cod);
            if (pos == -1) {
                System.out.println("Cliente nao encontrado!");
                return;
            }
            arq.seek(pos);
            Cliente c = lerCliente(arq);
            if (c.ativo != 'S') {
                System.out.println("Cliente esta inativo.");
                return;
            }
            exibirCliente(c);
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void alterarCliente() {
        try (RandomAccessFile arq = new RandomAccessFile(NOME_ARQUIVO, "rw"); Scanner leia = new Scanner(System.in)) {
            System.out.print("Informe o codigo do cliente a alterar: ");
            int cod = leia.nextInt();
            long pos = buscarClientePorCodigo(arq, cod);
            if (pos == -1) {
                System.out.println("Cliente nao encontrado!");
                return;
            }
            arq.seek(pos);
            Cliente c = lerCliente(arq);
            if (c.ativo != 'S') {
                System.out.println("Cliente esta inativo.");
                return;
            }
            leia.nextLine();
            System.out.print("Novo nome (min. 10 caracteres): ");
            c.nomeCliente = leia.nextLine();
            System.out.print("Novo valor de compra: ");
            c.vlrCompra = leia.nextFloat();
            System.out.print("Novo ano da primeira compra: ");
            c.anoPrimeiraCompra = leia.nextInt();
            System.out.print("Em dia (S/N): ");
            c.emDia = (leia.next().toUpperCase().charAt(0) == 'S');

            arq.seek(pos);
            gravarCliente(arq, c);
            System.out.println("Cliente alterado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void excluirCliente() {
        try (RandomAccessFile arq = new RandomAccessFile(NOME_ARQUIVO, "rw"); Scanner leia = new Scanner(System.in)) {
            System.out.print("Informe o codigo do cliente a excluir: ");
            int cod = leia.nextInt();
            long pos = buscarClientePorCodigo(arq, cod);
            if (pos == -1) {
                System.out.println("Cliente nao encontrado!");
                return;
            }
            arq.seek(pos);
            Cliente c = lerCliente(arq);
            if (c.ativo != 'S') {
                System.out.println("Cliente ja esta inativo.");
                return;
            }
            c.ativo = 'N';
            arq.seek(pos);
            gravarCliente(arq, c);
            System.out.println("Cliente excluido com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void exibirCliente(Cliente c) {
        System.out.println("\n--- Dados do Cliente ---");
        System.out.println("Codigo: " + c.codCliente);
        System.out.println("Nome: " + c.nomeCliente);
        System.out.println("Valor da Compra: " + c.vlrCompra);
        System.out.println("Ano da 1a Compra: " + c.anoPrimeiraCompra);
        System.out.println("Em dia: " + (c.emDia ? "Sim" : "Nao"));
    }

    public static long buscarClientePorCodigo(RandomAccessFile arq, int codBusca) throws IOException {
        arq.seek(0);
        while (arq.getFilePointer() < arq.length()) {
            long pos = arq.getFilePointer();
            char ativo = arq.readChar();
            int cod = arq.readInt();
            String nome = arq.readUTF();
            float compra = arq.readFloat();
            int ano = arq.readInt();
            boolean emDia = arq.readBoolean();
            if (cod == codBusca) return pos;
        }
        return -1;
    }

    public static void gravarCliente(RandomAccessFile arq, Cliente c) throws IOException {
        arq.writeChar(c.ativo);
        arq.writeInt(c.codCliente);
        arq.writeUTF(c.nomeCliente);
        arq.writeFloat(c.vlrCompra);
        arq.writeInt(c.anoPrimeiraCompra);
        arq.writeBoolean(c.emDia);
    }

    public static Cliente lerCliente(RandomAccessFile arq) throws IOException {
        Cliente c = new Cliente();
        c.ativo = arq.readChar();
        c.codCliente = arq.readInt();
        c.nomeCliente = arq.readUTF();
        c.vlrCompra = arq.readFloat();
        c.anoPrimeiraCompra = arq.readInt();
        c.emDia = arq.readBoolean();
        return c;
    }
}
