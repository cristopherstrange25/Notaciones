package com.kirascript.notaciones;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Conversor y Calculador de Expresiones");

        Label infixLabel = new Label("Ingresa la expresión infija:");
        TextField infixInput = new TextField();
        Button convertButton = new Button("Convertir y Resolver");
        Button clearButton = new Button("Limpiar");

        Label postfixLabel = new Label("Expresión Postfija:");
        TextField postfixOutput = new TextField();
        postfixOutput.setEditable(false);

        Label resultLabel = new Label("Resultado:");
        TextField resultOutput = new TextField();
        resultOutput.setEditable(false);

        // Evento para el botón "Convertir y Resolver"
        convertButton.setOnAction(event -> {
            try {
                String infixExpression = infixInput.getText();
                String postfixExpression = infixToPostfix(infixExpression);
                postfixOutput.setText(postfixExpression);

                double result = evaluatePostfix(postfixExpression);
                resultOutput.setText(String.valueOf(result));
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Error en la expresión");
                errorAlert.setContentText("Por favor, ingresa una expresión válida.");
                errorAlert.showAndWait();
            }
        });

        // Evento para el botón "Limpiar"
        clearButton.setOnAction(event -> {
            infixInput.clear();
            postfixOutput.clear();
            resultOutput.clear();
        });

        VBox layout = new VBox(10, infixLabel, infixInput, convertButton, clearButton, postfixLabel, postfixOutput, resultLabel, resultOutput);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para convertir de infijo a postfijo
    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char c : infix.toCharArray()) {
            if (Character.isDigit(c) || Character.isLetter(c)) {
                postfix.append(c).append(' ');
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.pop(); // Descartar '('
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString().trim();
    }

    // Método para evaluar una expresión postfija
    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        for (String token : postfix.split("\\s+")) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperation(a, b, token.charAt(0)));
            }
        }
        return stack.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private double applyOperation(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("División por cero");
                return a / b;
            default:
                throw new UnsupportedOperationException("Operador no soportado: " + operator);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
