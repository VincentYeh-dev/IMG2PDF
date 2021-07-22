package org.vincentyeh.IMG2PDF.concrete.util;

import org.fusesource.jansi.Ansi;

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.lang.String.format;
import static org.fusesource.jansi.Ansi.ansi;

public class PrinterUtils {

    public static Ansi getColorStackTrance(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return getColor(sw.toString(), Ansi.Color.RED);
    }

    public static Ansi getColor(String msg, Ansi.Color color) {
        return ansi().fg(color).a(msg).reset();
    }

    public static void printColor(String msg, Ansi.Color color) {
        print(getColor(msg, color));
    }

    public static void printColorFormat(String msg, Ansi.Color color, Object... objects) {
        print(getColor(format(msg, objects), color));
    }

    public static void printLine(Object object) {
        System.out.println(object);
    }

    public static void print(Object object) {
        System.out.print(object);
    }

    public static void backDel(int n){
        for(int i=0;i<n;i++){
            print("\b \b");
        }
    }

    public static void printStackTrance(Exception e) {
        printLine(getColorStackTrance(e));
    }

    public static void printRenderFormat(String msg, Object... objects) {
        print(getRenderFormat(msg, objects));
    }

    public static Ansi getRenderFormat(String msg, Object... objects) {
        return ansi().render(format(msg, objects));
    }
}
