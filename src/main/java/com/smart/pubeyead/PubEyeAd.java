package com.smart.pubeyead;

import com.smart.pubeyead.controller.IncomeListCreater;
import com.smart.pubeyead.utils.Constants;
import com.smart.pubeyead.utils.PropertyOperator;
import com.smart.pubeyead.view.JFontDialog;
import com.smart.pubeyead.view.MultiTaskView;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.collections.functors.ExceptionPredicate;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class PubEyeAd {
    static public void main(String[] args) {
        try {
            // Create a Parser
            CommandLineParser parser = new BasicParser( );
            Options options = new Options( );
            options.addOption("h", "help", false, "Print this usage information");
            options.addOption("c", "file", true, "File to save program output to");
            // Parse the program arguments
            CommandLine commandLine = parser.parse( options, args );

            // Set the appropriate variables based on supplied options
            if( commandLine.hasOption('h') ) {
                String msg = "m.jar -c set.propertiese ";
                JOptionPane.showMessageDialog(null, msg, "提示", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            Properties prop = null;
            if( commandLine.hasOption('c') ) {
                String propFile = commandLine.getOptionValue('c');
                prop = PropertyOperator.readProperties(propFile);
                prop.setProperty(Constants.PROPERTY_FILE, propFile);
            }

            MultiTaskView mtv = new MultiTaskView(prop);
            mtv.show();
        } catch(Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            JOptionPane.showMessageDialog(null, msg, "提示",JOptionPane.ERROR_MESSAGE);
        }

        return;
    }
}
