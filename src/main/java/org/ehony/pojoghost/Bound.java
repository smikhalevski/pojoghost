/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import java.lang.reflect.*;
import java.util.*;

public class Bound
{

    /**
     * Actual (raw) type of this bound.
     */
    private Type definition;
    /**
     * Class associated with this bound.
     */
    private Class<?> type;
    private Bound parent;
    private Bound superclass;
    private Set<Bound> interfaces = new HashSet<Bound>();
    private List<Bound> parameters = new ArrayList<Bound>();
    /**
     * Raw information on expected and received type parameters.
     */
    private List<GenericParameter> genericParameters = new ArrayList<GenericParameter>();

    private static class GenericParameter
    {

        public Type expected, received;

        private GenericParameter(Type type) {
            if (type == null) {
                throw new NullPointerException("Expected generic parameter cannot be empty.");
            }
            expected = type;
        }
    }

    /**
     * Get raw definition that bound represents.
     */
    public Type getDefinition() {
        return definition;
    }

    /**
     * Returns <code>true</code> if this bound represents composition of classes
     * and thus does not have any explicit reference {@link Class}.
     */
    public boolean isComposition() {
        return type == null && !(superclass == null && interfaces.isEmpty());
    }

    /**
     * {@inheritDoc}
     * <p>Can be {@code null} for bounds describing composition of classes.</p>
     * @see #isComposition()
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Bound holding this bound as a child.
     * <p>Bounds can be recursive: <code>A.superclass == B</code> but <code>B</code>
     * is not necessarily points to <code>A</code> as a parent.</p>
     */
    public Bound getParent() {
        return parent;
    }

    /**
     * Get list of bounds representing actual generic parameters of provided
     * to parameterised type.
     */
    public List<Bound> getParameters() {
        return parameters;
    }

    /**
     * Get bound of extended superclass.
     */
    public Bound getSuperclass() {
        return superclass;
    }

    /**
     * Get bounds of explicitly implemented interfaces.
     */
    public Set<Bound> getInterfaces() {
        return interfaces;
    }

    /**
     * Inserts data stored in {@link Class} object into bound
     * omitting generic parameter resolution.
     * <p>Applicable for both for anonymous and non-parameterised classes.</p>
     */
    private static void populateClassBound(Bound bound, Class<?> type) {
        bound.type = type;
        bound.definition = type;
        for (Type t : type.getTypeParameters()) {
            bound.genericParameters.add(new GenericParameter(t));
        }
        bound.superclass = traverse(bound, type.getGenericSuperclass());
        for (Type t : type.getGenericInterfaces()) {
            bound.interfaces.add(traverse(bound, t));
        }
    }

    /**
     * Returns resolved bound for {@link ParameterizedType}.
     */
    private static Bound traverseParameterisedBound(Bound parent, ParameterizedType type) {
        // Used traversing for future compatibility.
        // Optionally explicit cast to Class can be done because getRawType()
        // returns the type representing the class or interface that declared
        // type of parameterised type.
        Bound b = traverse(parent, type.getRawType());
        int i = 0;
        for (Type t : type.getActualTypeArguments()) {
            b.genericParameters.get(i++).received = t;
        }
        b.definition = type;
        b.parent = parent;
        return b;
    }

    /**
     * Returns resolved bound for {@link GenericArrayType}.
     */
    private static Bound traverseGenericArrayBound(Bound parent, GenericArrayType type) {
        Bound b = traverse(parent, type.getGenericComponentType());
        b.definition = type;
        b.parent = parent;
        return b;
    }

    /**
     * Returns resolved bound for type described by given bounds.
     */
    private static Bound traverseBoundedType(Bound parent, Type type, Type[] bounds) {
        Bound b;
        if (bounds.length == 1) {
            b = traverse(parent, bounds[0]);
        } else {
            b = new Bound();
            if (bounds.length == 0) {
                populateClassBound(b, Object.class);
            } else {
                for (Type t : bounds) {
                    Bound bound = traverse(parent, t);
                    if (bound.type.isInterface()) {
                        b.interfaces.add(bound);
                    } else {
                        b.superclass = bound;
                    }
                }
            }
        }
        b.definition = type;
        b.parent = parent;
        return b;
    }

    /**
     * Returns resolved bound for {@link WildcardType}.
     */
    private static Bound traverseWildcardType(Bound parent, WildcardType type) {
        return traverseBoundedType(parent, type, type.getUpperBounds());
    }

    /**
     * Returns resolved bound for {@link TypeVariable}.
     */
    private static Bound traverseTypeVariable(Bound parent, TypeVariable type) {
        return traverseBoundedType(parent, type, type.getBounds());
    }

    private static Bound lookupTypeVariable(Bound parent, TypeVariable type) {
        GenericParameter parameter = null;
        for (GenericParameter g : parent.genericParameters) {
            if (g.expected == type || g.received == type) {
                parameter = g;
                break;
            }
        }
        if (parameter != null && parent.parent != null) {
            for (GenericParameter g : parent.parent.genericParameters) {
                if (g.expected instanceof TypeVariable && ((TypeVariable) g.expected).getName().equals(type.getName())) {
                    if (g.received != null) {
                        return traverse(parent.parent, g.received);
                    } else {
                        return traverseTypeVariable(parent.parent, (TypeVariable) g.expected);
                    }
                }
            }
        }
        return traverseTypeVariable(parent, type);
    }














    public static Bound traverse(Type type) {
        return traverse(null, type);
    }

    protected static Bound traverse(Bound parent, Type type) {
        if (type == null) {
            return null;
        }
        Bound b = null;
        if (type instanceof TypeVariable) {
            b = lookupTypeVariable(parent, (TypeVariable) type);
        }
        if (type instanceof WildcardType) {
            b = traverseWildcardType(parent, (WildcardType) type);
        }
        if (type instanceof GenericArrayType) {
            b = traverseGenericArrayBound(parent, (GenericArrayType) type);
        }
        if (type instanceof ParameterizedType) {
            b = traverseParameterisedBound(parent, (ParameterizedType) type);
        }
        if (type instanceof Class) {
            b = new Bound();
            b.parent = parent;
            populateClassBound(b, (Class) type);
        }
        if (b == null) {
            throw new IllegalArgumentException("Unexpected type: " + type);
        }
        for (GenericParameter g : b.genericParameters) {
            if (g.received != null) {
                b.parameters.add(traverse(b, g.received));
            } else {
                b.parameters.add(traverse(b, g.expected));
            }
        }
        return b;
    }

//    // <editor-fold desc="Debug">
//
//
//    @Override
//    public String toString() {
//        return toString(getDebugInfo());
//    }
//
//    protected String getDebugInfo() {
//        for (Bound b = parent; b != null; b = b.parent) {
//            if (type == b.type) {
//                return "(cyclic class definition)";
//            }
//        }
//        StringBuilder out = new StringBuilder();
//        if (!parameters.isEmpty()) {
//            StringBuilder code = new StringBuilder();
//            for (Bound b : parameters) {
//                code.append(",\n").append(b);
//            }
//
//            String c = code.substring(2).replace("\n", "\n\t");
//            if (c.contains("\n")) {
//                out.append("\n<\n\t").append(c).append("\n>");
//            } else {
//                out.append('<').append(c).append('>');
//            }
//        }
//        if (superclass != null) {
//            out.append("\nextends ").append(superclass);
//        }
//        if (!interfaces.isEmpty()) {
//            StringBuilder code = new StringBuilder();
//            for (Bound b : interfaces) {
//                code.append(",\n").append(b);
//            }
//            String c = code.substring(2).replace("\n", "\n\t");
//            if (c.contains("\n")) {
//                out.append("\nimplements [\n\t").append(c).append("\n]");
//            } else {
//                out.append("\nimplements ").append(c);
//            }
//        }
//        return out.toString();
//    }
//
//    /**
//     * Returns string with formatted debug information about this tag and its children.
//     *
//     * @param info line feed separated parameters to display for this tag.
//     * @see #getDebugInfo()
//     */
//    protected String toString(String info) {
//        StringBuilder out = new StringBuilder();
//        if (type != null) {
//            out.append(((Class) type).getName());
//        }
//        if (info != null) {
//            if (info.contains("\n")) {
//                // Indenting info only when it contains line feeds.
//                out.append(info.replace("\n", "\n\t")).append('\n');
//            } else {
//                out.append(info);
//            }
//        }
//        return out.toString();
//    }
//    // </editor-fold>

}
