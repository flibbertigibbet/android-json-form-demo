package com.azavea.prs.driver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.azavea.prs.driver.schemas.DriverSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;

import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.azavea.prs.driver.schemas.*;

public class MainActivity extends AppCompatActivity {


    //ExampleSchema mySchema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = loadRecord();
                Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    String loadRecord() {

        // open JSON record example file in assets dir
        try {
            BufferedReader ir = new BufferedReader(new InputStreamReader(getAssets()
                    .open("json/data/DriverRecord.json"), "UTF-8"));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = ir.readLine()) != null) {
                stringBuilder.append(line);
            }
            ir.close();
            String responseStr = stringBuilder.toString();

            Log.d("MainActivity:loadRecord", responseStr);

            Gson gson = new GsonBuilder().create();

            DriverSchema record = gson.fromJson(responseStr, DriverSchema.class);

            if (record == null) {
                Log.e("MainActivity:loadRecord", "NO RECORD FOUND GAAAAAH!");
                return "Got nuthin";
            }

            AccidentDetails deets = record.getAccidentDetails();

            String wat = gson.toJson(record, DriverSchema.class);
            Log.d("MainActivity:loadRecord", wat);

            Vehicle vehicle = record.getVehicle().get(0);
            if (vehicle != null) {
                String plateNo = vehicle.getPlateNumber();
                if (plateNo != null) {
                    Log.d("MainActivity:loadRecord", "Got vehicle plate #" + plateNo);
                }
            }

            if (deets == null) {
                Log.e("MainActivity:loadRecord", "NO ACCIDENT DETAILS FOUND GAAAAAH!");

                return "Got no deets?!?";
            }

            AccidentDetails.Severity severity = deets.getSeverity();

            if (severity == null) {
                Log.e("MainActivity:loadRecord", "NO SEVERITY FOUND GAAAAAH!");
                return "Got no severity?!?";
            }
            Log.d("MainActivity:loadRecord", "Read accident with severity: " + severity.name());

            Field[] deetFields = AccidentDetails.class.getDeclaredFields();

            Log.d("loadrecord", "Looking into deets...");

            if (deetFields.length == 0) {
                Log.d("loadrecord", "No fields on deets?");
            }
            for (Field fld : deetFields) {
                String name = fld.getName();
                Annotation[] annotations = fld.getDeclaredAnnotations();
                for (Annotation annotation: annotations) {

                    Log.d("MainActivity", "Details Field " + name + " has annotation " + annotation.toString());
                }
            }
            return severity.name();

        } catch (IOException e) {
            e.printStackTrace();
            return "Something broke.";
        }
    }

    void buildSchema() {

        JCodeModel codeModel = new JCodeModel();

        /*
        URL source = null;
        try {
            source = new URL("file:///res/values/json/schemas/example_schema.json");

            GenerationConfig config = new DefaultGenerationConfig() {
                @Override
                public boolean isGenerateBuilders() { // set config option by overriding method
                    return true;
                }
            };

            SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(), new SchemaStore()), new SchemaGenerator());
            mapper.generate(codeModel, "MySchema", "com.azavea.prs", source);



            codeModel.build(new File(getFilesDir() + "/MyFooOutput"));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


    }

}
