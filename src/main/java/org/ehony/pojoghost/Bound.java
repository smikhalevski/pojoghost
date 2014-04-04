package org.ehony.pojoghost;

import java.beans.Introspector;
import java.lang.reflect.*;
import java.util.*;

public class Bound
{

    private Type definition;
    private String name;
    private Class<?> type;
    private Bound parent;
    private Bound superclass;
    private Set<Bound> interfaces = new HashSet<Bound>();
    private List<Bound> parameters = new ArrayList<Bound>();


    protected Bound copy() {
        Bound b = new Bound();
        b.definition = definition;
        b.name = name;
        b.type = type;
        b.parent = parent;
        b.superclass = superclass;
        b.interfaces = interfaces;
        b.parameters = parameters;
        return b;
    }

    public static Bound traverse(Bound parent, Type type) {
        if (type instanceof Class) {
            return traverse(parent, (Class) type);
        }
        if (type instanceof ParameterizedType) {
            return traverse(parent, (ParameterizedType) type);
        }
        if (type instanceof TypeVariable) {
            return traverse(parent, (TypeVariable) type);
        }
        if (type instanceof WildcardType) {
            return traverse(parent, (WildcardType) type);
        }
        if (type instanceof GenericArrayType) {
            return traverse(parent, (GenericArrayType) type);
        }
        return null;
    }


    private static Bound traverse(Bound parent, Class<?> type) {
        for (Bound b = parent; b != null; b = b.parent) {
            if (type == b.type) {
                b = b.copy();
                b.parent = parent;
                return b;
            }
        }
        Bound b = traverse(null, type, type.getTypeParameters());
        b.parent = parent;
        return b;
    }

    private static Bound traverse(Bound parent, ParameterizedType type) {
        Bound b = traverse(parent, (Class) type.getRawType(), type.getActualTypeArguments());
        b.definition = type;
        return b;
    }

    private static Bound traverse(Bound parent, GenericArrayType type) {
        Bound b = traverse(parent, type.getGenericComponentType());
        b.definition = type;
        return b;
    }

    private static Bound traverse(Bound parent, Class<?> type, Type[] parameters) {
        Bound b = new Bound();
        b.definition = type;
        b.type = type;
        b.parent = parent;
        TypeVariable<?>[] variables = type.getTypeParameters();
        int i = 0;
        for (Type t : parameters) {
            Bound bound = traverse(b, t);
            if (variables[i++] instanceof TypeVariable) {
                bound.name = ((TypeVariable) variables[i-1]).getName();
            }
            b.parameters.add(bound);
        }
        b.superclass = traverse(b, type.getGenericSuperclass());
        for (Type t : type.getGenericInterfaces()) {
            b.interfaces.add(traverse(b, t));
        }
        return b;
    }

    private static Bound traverse(Bound owner, TypeVariable<?> type) {
        String name = type.getName();
        for (Bound b : owner.parameters) {
            if (name.equals(b.name)) {
                return b;
            }
        }
        if (owner.parent != null) {
            Bound b = traverse(owner.parent, type);
            if (b != null) {
                return b;
            }
        }
        for (TypeVariable variable : owner.type.getTypeParameters()) {
            if (name.equals(variable.getName())) {
                Bound b;
                Type[] bounds = type.getBounds();
                switch (bounds.length) {
                    case 0:
                        b = traverse(owner, Object.class);
                        break;
                    case 1:
                        b = traverse(owner, bounds[0]);
                        break;
                    default:
                        b = new Bound();
                        for (Type t : bounds) {
                            Bound bound = traverse(b, t);
                            if (bound.type.isInterface()) {
                                b.interfaces.add(bound);
                            } else {
                                b.superclass = bound;
                            }
                        }
                        break;
                }
                b.parent = owner;
                b.definition = variable;
                b.name = name;
                return b;
            }
        }
        return null;
    }

    private static Bound traverse(Bound owner, WildcardType type) {
        Bound b;
        Type[] bounds = type.getUpperBounds();
        switch (bounds.length) {
            case 0:
                b = traverse(owner, Object.class);
                break;
            case 1:
                b = traverse(owner, bounds[0]);
                break;
            default:
                b = new Bound();
                for (Type t : bounds) {
                    Bound bound = traverse(b, t);
                    if (bound.type.isInterface()) {
                        b.interfaces.add(bound);
                    } else {
                        b.superclass = bound;
                    }
                }
                break;
        }
        b.parent = owner;
        b.definition = type;
        b.name = "?";
        return b;
    }


    @Override
    public String toString() {
        return toString(getDebugInfo());
    }

    protected String getDebugInfo() {
        for (Bound b = parent; b != null; b = b.parent) {
            if (type == b.type) {
                return "(recursive definition)";
            }
        }
        StringBuilder out = new StringBuilder();
        if (!parameters.isEmpty()) {
            StringBuilder code = new StringBuilder();
            for (Bound b : parameters) {
                code.append(",\n").append(b);
            }
            
            String c = code.substring(2).replace("\n", "\n\t");
            if (c.contains("\n")) {
                out.append("\n<\n\t").append(c).append("\n>");
            } else {
                out.append('<').append(c).append('>');
            }
        }
        if (superclass != null) {
            out.append("\nextends ").append(superclass);
        }
        if (!interfaces.isEmpty()) {
            StringBuilder code = new StringBuilder();
            for (Bound b : interfaces) {
                code.append(",\n").append(b);
            }
            
            String c = code.substring(2).replace("\n", "\n\t");
            if (c.contains("\n")) {
                out.append("\nimplements [\n\t").append(c).append("\n]");
            } else {
                out.append("\nimplements ").append(c);
            }
        }
        return out.toString();
    }


    /**
     * Returns string with formatted debug information about this tag and its children.
     *
     * @param info line feed separated parameters to display for this tag.
     * @see #getDebugInfo()
     */
    protected String toString(String info) {
        StringBuilder out = new StringBuilder();
        if (type != null || name != null) {
            if (name != null) {
                out.append(name);
            }
            if (type != null) {
                if (name != null) {
                    out.append(" = ");
                }
                out.append(type.getName());
            }
        }
        if (info != null) {
            if (info.contains("\n")) {
                // Indenting info only when it contains line feeds.
                out.append(info.replace("\n", "\n\t")).append('\n');
            } else {
                out.append(info);
            }
        }
        return out.toString();
    }


}
