/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost;

import org.ehony.pojoghost.api.Bound;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionBound implements Bound
{

    private Type type;
    private Bound parent; // Bound of superclass.
    private List<Bound>
            parameters = new ArrayList<Bound>(), // Genetic type parameters.
            interfaces = new ArrayList<Bound>(); // Implemented interfaces.

    private ReflectionBound() {
    }

    public ReflectionBound(Type type) {
        inspect(type, this, new HashMap<String, Bound>(), new HashMap<Type, Bound>());
    }

    /**
     * Inspect provided type and retrieve its structure.
     * @param type type to inspect.
     * @param target target bound to save data in.
     * @param variables cache of generic variable declarations accessible for given type.
     * @param cache cache of already parsed raw types.
     * @return Cached or <code>target</code> bound.
     */
    private static Bound inspect(Type type, ReflectionBound target, Map<String, Bound> variables, Map<Type, Bound> cache) {
        if (cache.containsKey(type)) {
            // Return immediately if type was already inspected and cached.
            return cache.get(type);
        } else {
            // Otherwise cache type description and proceed with its enrichment.
            cache.put(type, target);
        }

        if (target == null) {
            target = new ReflectionBound();
        }

        target.type = type;

        // <editor-fold desc="Inspection of Object.class">
        if (Object.class == type) {
            return target;
        }
        // </editor-fold>

        // <editor-fold desc="Inspection of A.class">
        if (type instanceof Class) {
            Class c = (Class) type;
            Map<String, Bound> context = new HashMap<String, Bound>();
            for (TypeVariable p : c.getTypeParameters()) {
                String name = p.getName();
                Bound b = variables.get(name);
                if (b == null) {
                    b = inspect(p, null, variables, cache);
                }
                context.put(name, b);
                target.parameters.add(b);
            }
            Type superclass = c.getGenericSuperclass();
            if (superclass != null) {
                target.parent = inspect(superclass, null, context, cache);
            }
            for (Type t : c.getGenericInterfaces()) {
                target.interfaces.add(inspect(t, null, context, cache));
            }
            return target;
        }
        // </editor-fold>

        // <editor-fold desc="Inspection of A[]">
        if (type instanceof GenericArrayType) {
            // Get type of array element, must not be empty.
            Type t = ((GenericArrayType) type).getGenericComponentType(); // A
            target.parameters.add(inspect(t, null, variables, cache));
            return target;
        }
        // </editor-fold>

        // <editor-fold desc="Inspection of A extends B & C">
        if (type instanceof TypeVariable) {
            TypeVariable v = (TypeVariable) type;
            String name = v.getName(); // A
            // If this variable was already inspected then return.
            if (variables.containsKey(name)) {
                return variables.get(name);
            }
            Type[] bounds = v.getBounds(); // [B, C]
            if (bounds.length > 1) {
                for (Type t : bounds) {
                    Bound b = inspect(t, null, variables, cache);
                    if (t instanceof Class) {
                        target.parent = b;
                    } else {
                        target.interfaces.add(b);
                    }
                }
                return target;
            } else {
                if (bounds.length == 0) {
                    // Any type may be provided to parameter with no upper bounds specified.
                    target.type = Object.class;
                    return target;
                } else {
                    return inspect(bounds[0], null, variables, cache);
                }
            }
        }
        // </editor-fold>

        // <editor-fold desc="Inspection of ? extends A">
        if (type instanceof WildcardType) {
            WildcardType w = (WildcardType) type;
            Type[] bounds = w.getUpperBounds();
            if (bounds.length > 1) {
                for (Type t : bounds) {
                    Bound b = inspect(t, null, variables, cache);
                    if (t instanceof Class) {
                        target.parent = b;
                    } else {
                        target.interfaces.add(b);
                    }
                }
                return target;
            } else {
                if (bounds.length == 0) {
                    // Any type may be provided to parameter with no upper bounds specified.
                    target.type = Object.class;
                    return target;
                } else {
                    return inspect(bounds[0], null, variables, cache);
                }
            }
        }
        // </editor-fold>

        // <editor-fold desc="Inspection of A<...>">
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            Class c = (Class) p.getRawType();
            target.type = c;
            
            Type[] parameters = p.getActualTypeArguments(); // Actual content of <...>.
            TypeVariable[] naming = c.getTypeParameters(); // Names of class parameters from class definition.
            
            Map<String, Bound> context = new HashMap<String, Bound>();
            
            // Inspect parameters of this parameterized class.
            // Array of actual parameters and their namings are parallel.
            for (int i = 0; i < parameters.length; i++) {
                context.put(naming[i].getName(), inspect(parameters[i], null, variables, cache));
            }
            return inspect(c, target, context, cache);
        }
        // </editor-fold>
        
        throw new IllegalArgumentException("Unexpected type.");
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public List<Bound> getParameters() {
        return parameters;
    }

    @Override
    public List<Bound> getInterfaces() {
        return interfaces;
    }

    @Override
    public Bound getParent() {
        return parent;
    }

    @Override
    public Bound findImplementedBoundOfType(Class<?> type) {
        if (this.type == type) {
            return this;
        }
        if (parent != null) {
            Bound b = parent.findImplementedBoundOfType(type);
            if (b != null) {
                return b;
            }
        }
        for (Bound b : interfaces) {
            b = b.findImplementedBoundOfType(type);
            if (b != null) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        if (type instanceof Class) {
            out.append(((Class) type).getSimpleName());
        }
        if (type instanceof GenericArrayType) {
            out.append("Array");
        }
        if (type instanceof TypeVariable) {
            out.append("Variable");
        }
        if (type instanceof WildcardType) {
            out.append("?");
        }
        if (type instanceof ParameterizedType) {
            out.append("ParameterizedType");
        }
        if (!parameters.isEmpty()) {
            out.append('<');
            for (int i = 0; i < parameters.size(); i++) {
                out.append(parameters.get(i));
                if (i < parameters.size() - 1) {
                    out.append(", ");
                }
            }
            out.append('>');
        }
        if (parent != null) {
            out.append(" extends ").append(parent);
        }
        if (!interfaces.isEmpty()) {
            out.append(" implements ");
            for (int i = 0; i < interfaces.size(); i++) {
                out.append(interfaces.get(i));
                if (i < interfaces.size() - 1) {
                    out.append(", ");
                }
            }
        }

        return out.toString();
    }
}