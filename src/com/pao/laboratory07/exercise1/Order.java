package com.pao.laboratory07.exercise1;

import java.util.ArrayDeque;
import java.util.Deque;

public class Order {
    private OrderState state;
    private final Deque<OrderState> previousStates;

    public Order(OrderState initialState) {
        this.state = initialState;
        this.previousStates = new ArrayDeque<OrderState>();
    }

    public boolean nextState() {
        if (isFinalState()) {
            return false;
        }

        previousStates.push(state);
        switch (state) {
            case PLACED:
                state = OrderState.PROCESSED;
                break;
            case PROCESSED:
                state = OrderState.SHIPPED;
                break;
            case SHIPPED:
                state = OrderState.DELIVERED;
                break;
            default:
                break;
        }

        System.out.println("Order state updated to: " + state);
        return true;
    }

    public boolean cancel() {
        if (isFinalState()) {
            return false;
        }

        previousStates.push(state);
        state = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
        return true;
    }

    public boolean undoState() {
        if (previousStates.isEmpty()) {
            return false;
        }

        state = previousStates.pop();
        System.out.println("Order state reverted to: " + state);
        return true;
    }

    private boolean isFinalState() {
        return state == OrderState.DELIVERED || state == OrderState.CANCELED;
    }
}
