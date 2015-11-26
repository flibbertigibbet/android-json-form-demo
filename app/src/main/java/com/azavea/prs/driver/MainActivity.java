package com.azavea.prs.driver;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.HibernateValidatorFactory;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.azavea.prs.driver.schemas.*;

import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ValidationProvider;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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


        //View mainContent = findViewById(R.id.content_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.content_main);
        // let layout size can change dynamically with content
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        String[] recordInfo = {"thing one", "thing two", "foo", "bar", "baz", "jazzy jeff",
                "fresh prince", "stravinsky", "beethoven", "Indiana", "New Hampshire", "Ohio",
                "gingerbread", "ice cream sandwich", "Finland", "Australia", "penguins"
        };
        mAdapter = new RecordAdapter(recordInfo);
        mRecyclerView.setAdapter(mAdapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                for (Annotation annotation : annotations) {

                    Log.d("MainActivity", "Details Field " + name + " has annotation " + annotation.toString());
                }
            }

            // let's try validation
            /*
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

            */

            //ValidatorFactory avf = Validation.buildDefaultValidatorFactory();
            //ValidatorFactory avf =
            //        Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();
            //Validation.byProvider(HibernateValidator.class).providerResolver(foo).configure();


            //Validation.byProvider(HibernateValidator.class).configure();

            ValidatorFactory validatorFactory = Validation
                    .byProvider(HibernateValidator.class)
                    .providerResolver(new ValidationProviderResolver() {
                        @Override
                        public List<ValidationProvider<?>> getValidationProviders() {
                            HibernateValidator v = new HibernateValidator();
                            return Collections.<ValidationProvider<?>>singletonList(new HibernateValidator());
                        }
                    })
                    .configure()
                    .ignoreXmlConfiguration()
                    /*
                    .messageInterpolator(new MessageInterpolator() {
                        @Override
                        public String interpolate(String messageTemplate, Context context) {
                            Log.d("MainActivity", "interpolating message template " + messageTemplate);
                            int id = ApplicationContext.getApplication().getResources().getIdentifier(messageTemplate, "string", R.class.getPackage().getName());
                            return ApplicationContext.getApplication().getString(id);
                        }

                        @Override
                        public String interpolate(String messageTemplate, Context context, Locale locale) {
                            return interpolate(messageTemplate, context);
                        }
                    })
                    */
                    .buildValidatorFactory();

            Validator validator = validatorFactory.getValidator();

            if (validator != null) {
                Log.d("MainActivity", "Yo got a validator. WHEEEEEEEEEEEEEE");
            } else {
                Log.d("MainActivity", "Ugh. GOT THIS FAR !!!!!!!!!!!!!!");
            }

            Set<ConstraintViolation<AccidentDetails>> errors = validator.validate(deets);

            if (errors.isEmpty()) {
                Log.d("MainActivity", "Hooray, deets is valid");
            } else {
                for (ConstraintViolation<AccidentDetails> error : errors) {
                    Log.d("MainActivity", "Got constraint violation for deets:" + error.getMessage());
                }
            }

            Log.d("MainActivity", "Let's do that again");

            // introduce error
            deets.setSeverity(null);
            deets.setLocalId("IAMNOTAVALIDID");

            errors = validator.validate(deets);

            if (errors.isEmpty()) {
                Log.d("MainActivity", "Deets is still valid?!? should not be...........");
            } else {
                for (ConstraintViolation<AccidentDetails> error : errors) {
                    Log.d("MainActivity", "Got constraint violation for deets:" + error.getMessage());
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.azavea.prs.driver/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.azavea.prs.driver/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
