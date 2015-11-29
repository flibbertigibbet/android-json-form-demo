package com.azavea.prs.driver;

import android.os.AsyncTask;
import android.util.Log;

import org.hibernate.validator.HibernateValidator;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.spi.ValidationProvider;

/**
 * Created by kat on 11/28/15.
 */
@SuppressWarnings("unchecked")
public class ValidationTask<T> extends AsyncTask<T, String, Set<ConstraintViolation<T>>> {

    public interface ValidationCallbackListener {
        void callback(boolean haveErrors);
    }

    ValidationCallbackListener listener;

    private static Validator validator = getValidator();

    public ValidationTask(ValidationCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    protected Set<ConstraintViolation<T>> doInBackground(T... objects) {
        // executes in parallel on Gingerbread

        Log.d("ValidationTask", "in validation task...");

        T obj = objects[0];

        long startTime = System.currentTimeMillis();

        Set<ConstraintViolation<T>> errors = validator.validate(obj);

        for (ConstraintViolation<T> error : errors) {
            Log.d("ValidationTask", "Found constraint violation:" + error.getMessage());
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        Log.d("ValidationTask", "Total validation time: " +
                twoDForm.format(elapsedTime / 1000.0f) + " seconds");

        return errors;
    }

    protected void onPostExecute(Set<ConstraintViolation<T>> errors) {
        if (errors.isEmpty()) {
            Log.d("ValidationTask", "Hooray, object is valid");
            listener.callback(false);
        } else {
            Log.d("ValidationTask", "Validation errors found");
            listener.callback(true);
        }
    }

    private static Validator getValidator() {
        return Validation
                .byProvider(HibernateValidator.class)
                .providerResolver(new ValidationProviderResolver() {
                    @Override
                    public List<ValidationProvider<?>> getValidationProviders() {
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

                .buildValidatorFactory().getValidator();
    }
}
