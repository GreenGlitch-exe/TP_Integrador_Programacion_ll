package tpintegrado_sanchez_rios;

import java.util.Scanner;

public class AyudaScanner {
    private static Scanner sc = new Scanner(System.in);

    public static int readInt(String prompt, int min, int max) throws ExcepcionValorEntrada {
        System.out.print(prompt + " ");
        try {
            int val = Integer.parseInt(sc.nextLine());
            if (val < min || val > max) throw new ExcepcionValorEntrada("Valor fuera de rango");
            return val;
        } catch (NumberFormatException e) {
            throw new ExcepcionValorEntrada("Entrada no es un numero valido");
        }
    }

    public static double readDouble(String prompt, double min, double max) throws ExcepcionValorEntrada {
        System.out.print(prompt + " ");
        try {
            double val = Double.parseDouble(sc.nextLine());
            if (val < min || val > max) throw new ExcepcionValorEntrada("Valor fuera de rango");
            return val;
        } catch (NumberFormatException e) {
            throw new ExcepcionValorEntrada("Entrada no es un numero valido");
        }
    }

    public static String readString(String prompt) throws ExcepcionValorEntrada {
        System.out.print(prompt + " ");
        String s = sc.nextLine().trim();
        if (s.isEmpty()) throw new ExcepcionValorEntrada("Entrada vacia");
        return s;
    }
}

