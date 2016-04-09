/**
 * Created by luizamboni on 09/04/16.
 */
import static spark.Spark.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;


import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;


public class App {

    public static void main(String[] args) {
        get("/formula/:formula/size/:size", (req, res) ->{
            String formula = req.params(":formula");
            String tex = latexTemplate(formula);
            System.out.println(tex);
            TeXFormula texFormula = new TeXFormula(latexTemplate(tex));

            // Note: New interface using builder pattern (inner class):
            TeXIcon icon = texFormula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();

            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setColor(new Color(0, 0, 0, 1));
            g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
            JLabel jl = new JLabel();
            jl.setForeground(new Color(0, 0, 0));
            icon.paintIcon(jl, g2, 0, 0);

            File file = new File("Example1.png");
            try {
                ImageIO.write(image, "png", file.getAbsoluteFile());
                HttpServletResponse raw = res.raw();
                res.header("Content-Disposition", "attachment; filename=image.png");
//                res.type("application/force-download");
                raw.getOutputStream().write(getData(image));
                raw.getOutputStream().flush();
                raw.getOutputStream().close();
                return raw;

            } catch (IOException ex) {
                halt();

            }

           return res;



        });
    }

    public static String latexTemplate(String formula){
//        return  new StringBuilder()
//                .append("\\documentclass{article}\n")
//                .append("\\usepackage{amsmath}\n")
//                .append("\\usepackage{cancel}\n")
//                .append("\\usepackage{amsfonts}\n")
//                .append("\\usepackage{amssymb}\n")
//                .append("\\usepackage{amsthm}\n")
//                .append("\\usepackage{mathtools}\n")
//                .append("\\pagestyle{empty}\n")
//                .append("\\begin{document}\n")
//                .append("$$"+ formula + "$$\n")
//                .append("\\end{document}").toString();
        return  new StringBuilder()
                .append("$$"+ formula + "$$\n").toString();


    }


    public static byte[] getData(BufferedImage bi){

        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", buff);
        } catch (IOException ex) {
        }
        byte[] bytes = buff.toByteArray();

        return bytes;
    }
}
