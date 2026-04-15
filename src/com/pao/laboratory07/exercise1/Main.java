package com.pao.laboratory07.exercise1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Part A
        // load initial state
        OrderState initialState = OrderState.valueOf(scanner.next());
        Order order = new Order(initialState);
        System.out.println("Initial order state: " + initialState);

        while (true) {
            OrderCommand orderCommand = OrderCommand.valueOf(scanner.next());
            switch (orderCommand) {
                case next:
                    if (!order.nextState()) {
                        System.out.println("Order is already in a final state.");
                    }
                    break;
                case cancel:
                    if (!order.cancel()) {
                        System.out.println("Cannot cancel a final state order.");
                    }
                    break;
                case undo:
                    if (!order.undoState()) {
                        System.out.println("Cannot undo the initial order state.");
                    }
                    break;
                case QUIT:
                    System.out.println("User quit the program.");
                    return;
                default:
                    break;
            }
        }
    }
}
