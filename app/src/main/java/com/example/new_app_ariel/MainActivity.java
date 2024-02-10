package com.example.new_app_ariel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.textViewResult);
        result.setText("");
    }
    public float parseTextToFloat(String str){
        float num;
        num = Float.parseFloat(str);
        return num;
    }
    public void funcNumber(View view) {
        Button button = (Button) view;
        String str = button.getText().toString();
        result.append(str);
    }


    public void equals(View view) {
        String str = result.getText().toString();
        String finalResult = "";

        //result equation list
        ArrayList<String> equationList = new ArrayList<String>();

        String multiDivisionRegex = "[x/]";
        String subAdditionRegex = "[+-]";

        System.out.println(str);
        String[] calcChunks = str.split(subAdditionRegex);
        String[] ops = str.split("[0123456789.x/]");

        String subAdditionStr = "";

        //add + if the str starts with ""
        System.out.println("ops: " + Arrays.toString(ops));
        if (ops.length != 0) {
            if (ops[0].equals("")) {
                ops[0] = "+";
            }

            //convert to list to fix a bug
            ArrayList<String> calcChunksList = new ArrayList<String>(Arrays.asList(calcChunks));
            if (calcChunksList.get(0).equals(""))
                calcChunksList.remove(0);
            System.out.println("calcChunksList: " + Arrays.toString(calcChunksList.toArray()));
            System.out.println("ops: " + Arrays.toString(ops));



            //start building equation
            int j = 0;
            for (int i = 0; i < ops.length; i++) {
                if (!ops[i].equals("")) {
                    equationList.add(ops[i]);
                    equationList.add(calcChunksList.get(j++));
                }
            }
        }
        else {
            equationList.add("+");
            equationList.add(str);
        }
        System.out.println("equation: " + Arrays.toString(equationList.toArray()));

        for (int i = 0; i < equationList.size(); i++) {
            equationList.set(i, evaluateExpression(equationList.get(i), multiDivisionRegex));
        }
        System.out.println("equation after multi and divide: " + Arrays.toString(equationList.toArray()));

        subAdditionStr = TextUtils.join("", equationList);
        result.setText(evaluateExpression(subAdditionStr, subAdditionRegex));


    }

    public static String evaluateExpression(String expression, String reg) {
        if(expression.length() == 1) return expression;

        // Split the expression based on 'x' and '/'
        String[] parts = expression.split(reg);
        String[] ops = expression.split("[0123456789.]");

        // Initialize result with the first number
        float result;
        float signbit = 1;
        if (expression.charAt(0) == '-')
            signbit = -1;
        if(parts[0].equals(""))
            result = (Float.parseFloat(parts[1])) * signbit;
        else
            result = (Float.parseFloat(parts[0])) * signbit;

        // Iterate over the remaining parts and perform multiplication or division
        for (int i = 0; i < parts.length - 1; i++) {
            float nextNumber = Float.parseFloat(parts[i + 1]);
            if (ops[i + 1].equals(Character.toString(reg.charAt(1)))) {
                if (Character.toString(reg.charAt(1)).equals("x"))
                    result *= nextNumber;
                else
                    result += nextNumber;
            }
            else if (ops[i + 1].equals(Character.toString(reg.charAt(2)))) {
                if (Character.toString(reg.charAt(2)).equals("/")) {
                    if (nextNumber != 0) {
                        result /= nextNumber;
                    }
                    else {
                        throw new ArithmeticException("Cannot divide by zero");
                    }
                }
                else {
                    result -= nextNumber;
                }
            }
        }

        // If the result is a whole number, convert it to an integer
        if (result == (int) result) {
            return (Integer.toString((int) result));
        }

        return (Float.toString(result));
    }

    public void clear(View view) {
        result.setText("");
    }
}