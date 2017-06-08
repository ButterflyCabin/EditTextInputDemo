package com.yehu.edittextinputdemo.Utils;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 创建日期：2017/6/8 14:33
 *
 * @author yehu
 *         类说明：
 */
public class ViewInputUtils {

    public static void setOnContentTextCompleteListener(final TextView view, final int maxIntCount, final int accuracy, final OnContentTextCompleteListener listener) {
        view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString();
                if (!isBlank(amount)) { //输入框没内容，按钮不可点击
                    SparseArray<String> sparseArray = (SparseArray) view.getTag();
                    if (null == sparseArray) {
                        sparseArray = new SparseArray<String>();
                    }
                    boolean hasDecimalPoint = false;
                    int position = 0; // 当首位是小数点的时候，自动在小数点前添加0
                    String newInteger = "";// 整数部分
                    String newDecimal = "";// 小数部分
                    String oldInteger = sparseArray.get(0);// 整数部分
                    String oldDecimal = sparseArray.get(1);// 小数部分
                    boolean isMaxIntCount = false;
                    if (amount.contains(".")) {
                        hasDecimalPoint = true;
                        position = amount.indexOf(".");
                        newInteger = amount.substring(0, position);
                        newDecimal = amount.substring(position + 1, amount.length());
                    } else {
                        newInteger = amount;
                    }
                    if (isBlank(newInteger)) {
                        newInteger = hasDecimalPoint ? "0" : "";
                    } else if (newInteger.length() > 1 && "0".equals(newInteger.substring(0, 1))) {
                        newInteger = newInteger.substring(1, newInteger.length());
                    } else if (maxIntCount >= 0 && newInteger.length() > maxIntCount) {
                        newInteger = newInteger.substring(0, maxIntCount);
                        isMaxIntCount = true;
                    }
                    String newAmount;
                    if (!isBlank(newDecimal) && accuracy >= 0 && newDecimal.length() > accuracy) {
                        newDecimal = newDecimal.substring(0, accuracy);
                    } else{

                    }
                    if (!hasDecimalPoint) {
                        newAmount = newInteger;
                    } else if (isBlank(newDecimal)) {
                        newAmount = newInteger + ".";
                    } else {
                        newAmount = newInteger + "." + newDecimal;
                    }
                    sparseArray.put(0, newInteger);
                    sparseArray.put(1, newDecimal);
                    view.setTag(sparseArray);
                    if (!amount.equals(newAmount)) {
                        amount = newAmount;
                        view.setText(amount);
                        position = 0;
                        if (view instanceof EditText) {
                            if (!oldInteger.equals(newInteger)) {
                                position += compareString(oldInteger, newInteger);
                            } else if (isMaxIntCount) {
                                position += hasDecimalPoint ? newInteger.length() + 1 : newInteger.length();
                            } else if (oldDecimal.equals(newDecimal)) {
                                position += hasDecimalPoint ? newInteger.length() + 1 : newInteger.length();
                                position += newDecimal.length();
                            } else {
                                position += hasDecimalPoint ? newInteger.length() + 1 : newInteger.length();
                                position += compareString(oldDecimal, newDecimal);
                            }
                            ((EditText) view).setSelection(position);
                        }
                    }
                }
                if (null != listener)
                    listener.onComplete(!isBlank(amount), amount);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public interface OnContentTextCompleteListener {
        void onComplete(boolean isAvailable, String text);
    }

    public static boolean isBlank(CharSequence charSequence){
        if (null == charSequence || charSequence.toString().trim().length() <= 0){
            return  true;
        }
        return false;
    }

    public static int compareString (String strA,String strB){
        char[] tempA = strA.toCharArray();
        char[] tempB = strB.toCharArray();
        for (int i = 0; i < Math.min(tempB.length, tempA.length); i++) {
            if (tempA[i] != tempB[i]) {
                return i + 1;
            }
        }
        return 0;
    }
}
