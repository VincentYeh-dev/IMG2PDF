package org.vincentyeh.IMG2PDF.handler.concrete;

import org.vincentyeh.IMG2PDF.handler.framework.ExceptionHandler;
import org.vincentyeh.IMG2PDF.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.handler.framework.Handler;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ParameterExceptionHandler extends ExceptionHandler {

    public ParameterExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "parameter",resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {

        if (data instanceof CommandLine.ParameterException) {
            if (data instanceof CommandLine.UnmatchedArgumentException) {
                CommandLine.UnmatchedArgumentException exception = (CommandLine.UnmatchedArgumentException) data;
                String list = exception.getUnmatched().stream().map(str -> '\'' + str + '\'').collect(Collectors.joining(", "));
                return format(getLocaleString("unmatched_argument.unknown_option"), list);
            } else if (data instanceof CommandLine.MissingParameterException) {
                CommandLine.MissingParameterException exception = (CommandLine.MissingParameterException) data;
                List<String> param_list = exception.getMissing().stream()
                        .filter(CommandLine.Model.ArgSpec::isPositional)
                        .map(argSpec -> {
                            CommandLine.Model.PositionalParamSpec positionalParamSpec = (CommandLine.Model.PositionalParamSpec) argSpec;
                            return positionalParamSpec.paramLabel();
                        })
                        .collect(Collectors.toList());


                List<String> option_list = exception.getMissing().stream()
                        .filter(CommandLine.Model.ArgSpec::isOption)
                        .map(argSpec -> {
                            CommandLine.Model.OptionSpec optionSpec = (CommandLine.Model.OptionSpec) argSpec;
                            return optionSpec.longestName();
                        })
                        .collect(Collectors.toList());

                StringBuilder builder = new StringBuilder();
                if (!param_list.isEmpty())
                    builder.append(format(getLocaleString("missing_required.parameter") + '\n', String.join(", ", param_list)));

                if (!option_list.isEmpty())
                    builder.append(format(getLocaleString("missing_required.option") + '\n', String.join(", ", option_list)));

                return builder.toString();
            }else if(data.getMessage()!=null&&data.getMessage().contains("Missing required subcommand")){
                return getLocaleString("missing_required_subcommand");
            }else if (data.getCause() != null && data.getCause() instanceof CommandLine.TypeConversionException) {
                CommandLine.ParameterException ex = (CommandLine.ParameterException) data;
                CommandLine.Model.ArgSpec argSpec = ex.getArgSpec();
                Class<?> clazz = argSpec.type();

                CommandLine.Model.OptionSpec optionSpec = (CommandLine.Model.OptionSpec) argSpec;
                if (clazz.isEnum())
                    return format(getLocaleString("type_convert.enum"), getResourceArgSpecType(argSpec), optionSpec.longestName(), getEnumConstants(clazz), ex.getValue());
                else
                    return format(getLocaleString("type_convert.normal"), getResourceArgSpecType(argSpec), optionSpec.longestName(), ex.getValue());
            }
        }

        return doNext(data);
    }

    private String getResourceArgSpecType (CommandLine.Model.ArgSpec argSpec){
            if (argSpec.isPositional())
                return getPublicString("parameter");
            else if (argSpec.isOption())
                return getPublicString("option");
            return null;
        }

        private String getEnumConstants (Class < ? > clazz){
            if (!clazz.isEnum())
                throw new IllegalArgumentException("clazz is not a enum.");
            List<String> constants = Arrays.stream(clazz.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
            return String.join(", ", constants);
        }
    }
