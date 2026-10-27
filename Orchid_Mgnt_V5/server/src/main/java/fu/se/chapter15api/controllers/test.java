package fu.se.chapter15api.controllers;

import java.util.Scanner;

public class test {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt())
            return;

        int t = sc.nextInt();

        while (t-- > 0) {
            long a = sc.nextLong();
            long b = sc.nextLong();
            long c = sc.nextLong();

            long cop = c / 2;

            long v = cop * (a - b);

            if (c % 2 != 0) {
                v += a;
            }

            System.out.println(v);

        }

    }
}