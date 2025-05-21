import java.util.Scanner;
import java.time.LocalDate;

public class ControleMultasDetran {

    public static boolean dataEhValida(String data) {
        if (data.length() != 10 || data.charAt(2) != '/' || data.charAt(5) != '/') {
            return false;
        }

        try {
            int dia = Integer.parseInt(data.substring(0, 2));
            int mes = Integer.parseInt(data.substring(3, 5));
            int ano = Integer.parseInt(data.substring(6, 10));

            if (mes < 1 || mes > 12) return false;

            int anoAtual = LocalDate.now().getYear();
            if (ano > anoAtual) return false;

            int diasNoMes;
            switch (mes) {
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    diasNoMes = 31; break;
                case 4: case 6: case 9: case 11:
                    diasNoMes = 30; break;
                case 2:
                    if ((ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0)) {
                        diasNoMes = 29;
                    } else {
                        diasNoMes = 28;
                    }
                    break;
                default:
                    return false;
            }

            return dia >= 1 && dia <= diasNoMes;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean placaEhValida(String placa) {
        return placa.matches("[A-Z]{3}\\d{4}");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double somaMultas = 0;
        double menorMulta = Double.MAX_VALUE;
        int totalMultas = 0;

        while (true) {
            System.out.print("Digite a placa do veículo (ou 'sair' para encerrar): ");
            String placa = scanner.nextLine().toUpperCase();

            if (placa.equals("SAIR")) break;

            if (!placaEhValida(placa)) {
                System.out.println("Placa inválida. Formato correto: 3 letras + 4 números (Ex: ABC1234).");
                continue;
            }

            System.out.print("Digite a data da multa (DD/MM/AAAA): ");
            String data = scanner.nextLine();

            if (!dataEhValida(data)) {
                System.out.println("Data inválida.");
                continue;
            }

            System.out.print("Digite o valor da multa: ");
            double valorMulta = scanner.nextDouble();
            scanner.nextLine(); // Limpar buffer

            if (valorMulta <= 0) {
                System.out.println("Valor da multa deve ser maior que zero.");
                continue;
            }

            somaMultas += valorMulta;
            if (valorMulta < menorMulta) menorMulta = valorMulta;
            totalMultas++;
        }

        if (totalMultas > 0) {
            double media = somaMultas / totalMultas;
            System.out.printf("Soma total das multas: R$ %.2f%n", somaMultas);
            System.out.printf("Valor médio das multas: R$ %.2f%n", media);
            System.out.printf("Menor valor de multa: R$ %.2f%n", menorMulta);
        } else {
            System.out.println("Nenhuma multa registrada.");
        }

        scanner.close();
    }
}
