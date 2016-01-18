package org.squonk.options;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.squonk.notebook.api.OptionDefinition;
import org.squonk.notebook.api.OptionType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Describes a property that needs to be defined in order to execute a service.
 * <br>e.g. the threshold for a similarity search
 * <br>e.g. the query structure for a structure search
 * <p>
 * TODO - validation: can't use a method call for this as it may be specified from non-java
 * language. So maybe specify a regexp that can be used to validate the value (but this assumes it a
 * string) and also need to provide a reason why the value is invalid, or at least a description of
 * what types of values are valid
 * <p>
 *
 * @author Tim Dudgeon
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionDescriptor<T> implements OptionDefinition<T>, Serializable {

    public enum Visibility {

        EDITABLE, NON_EDITABLE, HIDDEN
    }

    private final TypeDescriptor<T> typeDescriptor;
    private final String key;
    private final String label;
    private final String description;
    private final T[] values;
    private final T defaultValue;
    private final Visibility visibility;
    private final Integer minValues;
    private final Integer maxValues;

    /** Full constructor.
     *
     * @param typeDescriptor The Java typeDescriptor of the option
     * @param key The code name of the option. Must be unique among a set of options.
     * @param label The name of the option as displayed to the user.
     * @param description A description of the option e.g. to be displayed as a tooltip.
     * @param values A list of legal values. If null then any value us allowed
     * @param defaultValue The default value. Can be null.
     * @param visibility How the option appears to the user. Allows options to be presented but not changed, or an option
     *                   to be defined but completely hidden from the user.
     * @param minValues The minimum number of values. If 0 then the option is optional. If 1 it is requiried. If greater than
     *                  1 then at least this many values need to be specified. If null then assumed to be 1.
     * @param maxValues The maximum number of values. If null then any number are allowed.
     */
    public OptionDescriptor(
            @JsonProperty("typeDescriptor") TypeDescriptor<T> typeDescriptor,
            @JsonProperty("key") String key,
            @JsonProperty("label") String label,
            @JsonProperty("description") String description,
            @JsonProperty("values") T[] values,
            @JsonProperty("defaultValue") T defaultValue,
            @JsonProperty("visibility") Visibility visibility,
            @JsonProperty("minValues") Integer minValues,
            @JsonProperty("maxValues") Integer maxValues
    ) {
        this.typeDescriptor = typeDescriptor;
        this.key = key;
        this.label = label;
        this.description = description;
        this.values = values;
        this.defaultValue = defaultValue;
        this.visibility = visibility;
        this.minValues = minValues;
        this.maxValues = maxValues;
    }

    public OptionDescriptor(TypeDescriptor<T> type, String key, String label, String description) {
        this(type, key, label, description, null, null, Visibility.EDITABLE, 1, null);
    }

    /** Create an OptionDescriptor whose typeDescriptor is a {@link SimpleTypeDescriptor&lt;T&gt;}
     *
     * @param type
     * @param key
     * @param label
     * @param description
     */
    public OptionDescriptor(Class<T> type, String key, String label, String description) {
        this(new SimpleTypeDescriptor<T>(type), key, label, description, null, null, Visibility.EDITABLE, 1, null);
    }

    public OptionDescriptor withDefaultValue(T defaultValue) {
        return new OptionDescriptor(typeDescriptor, key, label, description, values, defaultValue, visibility, minValues, maxValues);
    }

    public OptionDescriptor withValues(T[] values) {
        return new OptionDescriptor(typeDescriptor, key, label, description, values, defaultValue, visibility, minValues, maxValues);
    }

    public OptionDescriptor withVisibility(Visibility visibility) {
        return new OptionDescriptor(typeDescriptor, key, label, description, values, defaultValue, visibility, minValues, maxValues);
    }

    public OptionDescriptor withMinValues(int minValues) {
        return new OptionDescriptor(typeDescriptor, key, label, description, values, defaultValue, visibility, minValues, maxValues);
    }

    public OptionDescriptor withMaxValues(int maxValues) {
        return new OptionDescriptor(typeDescriptor, key, label, description, values, defaultValue, visibility, minValues, maxValues);
    }

    /**
     * The code name for the parameter. Will be returned as the key, with the user chosen value as
     * the value.
     *
     * @return
     */
    public String getkey() {
        return key;
    }

    /**
     * The label do use to identify the parameter in the UI
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * A description of the parameter to use as a tooltip in the UI
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    public TypeDescriptor<T> getTypeDescriptor() {
        return typeDescriptor;
    }

    /**
     * A list of legal values. Unless {@link #getDefaultValue} is specified the first option is the
     * default.
     *
     * @return
     */
    public T[] getValues() {
        return values;
    }

    /**
     * The default value to use. If {@link #getValues} is specified must be one of those values)
     *
     * @return
     */
    public T getDefaultValue() {
        return defaultValue;
    }


    /**
     * How the parameter should appear in the UI.
     * <ul>
     * <li>EDITABLE - the parameter is visible and editable.</li>
     * <li>NON_EDITABLE - the parameter is visible but not editable. A default must be
     * specified.</li>
     * <li>HIDDEN - the parameter is not displayed but its value is returned in the results. A
     * default must be specified.</li>
     * </ul>
     *
     * @return
     */
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return key;
    }

    @Override
    @JsonIgnore
    public String getDisplayName() {
        return label;
    }

    @Override
    @JsonIgnore
    public OptionType getOptionType() {
        if (maxValues != null && maxValues > 1) {
            return OptionType.PICKLIST;
        } else {
            return OptionType.SIMPLE;
        }
    }

    @Override
    @JsonIgnore
    public List<T> getPicklistValueList() {
        return values == null ? null : Arrays.asList(getValues());
    }
}
