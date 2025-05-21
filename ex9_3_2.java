import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class ContaTelefonica {

    // Método para validar a hora no formato HH:MM
    public static boolean horaEhValida(String hora) {
        if (hora == null || !hora.matches("\\d{2}:\\d{2}")) {
            return false;
        }
        String[] partes = hora.split(":");
        int hh = Integer.parseInt(partes[0]);
        int mm = Integer.parseInt(partes[1]);
        return (hh >= 0 && hh <= 23) && (mm >= 0 && mm <= 59);
    }

    // Método para converter hora em string para LocalTime
    public static LocalTime converterParaLocalTime(String hora) {
        return LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));
    }

    // Método para determinar o custo por minuto com base no horário inicial
    public static double obterCustoPorMinuto(LocalTime horario) {
        int hora = horario.getHour();
        if (hora >= 0 && hora <= 5) return 0.10;
        if (hora >= 6 && hora <= 7) return 0.15;
        if (hora >= 8 && hora <= 17) return 0.20;
        return 0.15; // entre 18h e 23:59
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double totalConta = 0.0;

        while (true) {
            System.out.println("\nDigite o horário inicial da ligação (HH:MM) ou 'fim' para encerrar:");
            String horaInicialStr = scanner.nextLine();

            // Flag para encerrar
            if (horaInicialStr.equalsIgnoreCase("fim")) {
                break;
            }

            // Validação do horário inicial
            if (!horaEhValida(horaInicialStr)) {
                System.out.println("Horário inicial inválido! Tente novamente.");
                continue;
            }

            System.out.println("Digite o horário final da ligação (HH:MM):");
            String horaFinalStr = scanner.nextLine();

            // Validação do horário final
            if (!horaEhValida(horaFinalStr)) {
                System.out.println("Horário final inválido! Tente novamente.");
                continue;
            }

            // Conversão para LocalTime
            LocalTime horaInicial = converterParaLocalTime(horaInicialStr);
            LocalTime horaFinal = converterParaLocalTime(horaFinalStr);

            // Verificação de sequência lógica
            if (!horaFinal.isAfter(horaInicial)) {
                System.out.println("Erro: o horário final deve ser após o horário inicial!");
                continue;
            }

            // Cálculo da duração em minutos
            long duracaoMinutos = ChronoUnit.MINUTES.between(horaInicial, horaFinal);

            // Obtenção do custo por minuto
            double custoPorMinuto = obterCustoPorMinuto(horaInicial);

            // Cálculo do custo da ligação
            double custoLigacao = duracaoMinutos * custoPorMinuto;

            // Exibição e soma do total
            System.out.printf("Duração: %d minutos | Custo: R$ %.2f\n", duracaoMinutos, custoLigacao);
            totalConta += custoLigacao;
        }

        System.out.printf("\nValor TOTAL da conta telefônica: R$ %.2f\n", totalConta);
        scanner.close();
    }
}