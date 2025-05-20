import java.util.Scanner;

public class ContaTelefonica {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double totalConta = 0.0;
        String horarioInicial, horarioFinal;

        while (true) {
            System.out.println("Digite o horário inicial da chamada (HH:MM) ou 'FIM' para encerrar:");
            horarioInicial = scanner.nextLine();

            if (horarioInicial.equalsIgnoreCase("FIM")) {
                break;
            }

            if (!horaEhValida(horarioInicial)) {
                System.out.println("Horário inicial inválido! Tente novamente.");
                continue;
            }

            System.out.println("Digite o horário final da chamada (HH:MM):");
            horarioFinal = scanner.nextLine();

            if (!horaEhValida(horarioFinal)) {
                System.out.println("Horário final inválido! Tente novamente.");
                continue;
            }

            if (!horarioFinalEhMaior(horarioInicial, horarioFinal)) {
                System.out.println("O horário final deve ser maior que o horário inicial! Tente novamente.");
                continue;
            }

            int duracaoMinutos = calculaDuracao(horarioInicial, horarioFinal);
            double custoMinuto = calculaCustoPorMinuto(horarioInicial);
            double custoChamada = duracaoMinutos * custoMinuto;

            System.out.printf("Duração da chamada: %d minutos\n", duracaoMinutos);
            System.out.printf("Custo da chamada: R$ %.2f\n", custoChamada);

            totalConta += custoChamada;
        }

        System.out.printf("Valor total da conta telefônica: R$ %.2f\n", totalConta);
        scanner.close();
    }

    public static boolean horaEhValida(String horario) {
        if (horario == null || !horario.matches("\\d{2}:\\d{2}")) {
            return false;
        }
        String[] partes = horario.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
    }

    public static boolean horarioFinalEhMaior(String horarioInicial, String horarioFinal) {
        return horarioFinal.compareTo(horarioInicial) > 0;
    }

    public static int calculaDuracao(String horarioInicial, String horarioFinal) {
        String[] partesInicial = horarioInicial.split(":");
        String[] partesFinal = horarioFinal.split(":");

        int horaInicial = Integer.parseInt(partesInicial[0]);
        int minutoInicial = Integer.parseInt(partesInicial[1]);
        int horaFinal = Integer.parseInt(partesFinal[0]);
        int minutoFinal = Integer.parseInt(partesFinal[1]);

        int minutosInicial = horaInicial * 60 + minutoInicial;
        int minutosFinal = horaFinal * 60 + minutoFinal;

        return minutosFinal - minutosInicial;
    }

    public static double calculaCustoPorMinuto(String horarioInicial) {
        String[] partes = horarioInicial.split(":");
        int horaInicial = Integer.parseInt(partes[0]);

        if (horaInicial >= 0 && horaInicial <= 5) {
            return 0.10;
        } else if (horaInicial >= 6 && horaInicial <= 7) {
            return 0.15;
        } else if (horaInicial >= 8 && horaInicial <= 17) {
            return 0.20;
        } else { // 18:00 às 23:59
            return 0.15;
        }
    }
}