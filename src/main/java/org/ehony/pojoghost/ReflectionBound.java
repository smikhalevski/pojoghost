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

public class ReflectionBound<C> implements Bound<C>
{

    private Type type;
    private List<Bound<?>>
            parameters = new ArrayList<Bound<?>>(), // Genetic type parameters.
            parents = new ArrayList<Bound<?>>(); // Extended superclasses and implemented interfaces.

    private ReflectionBound() {
    }

    public ReflectionBound(Type type) {
        inspect(type, this, new HashMap<String, Bound>(), new HashMap<Type, Bound>());
    }

    
    
    
    
    private static ReflectionBound createBound(Type type, ReflectionBound target) {
        if (target == null) {
            target = new ReflectionBound();
        }
        target.rawType = type;
        return target;
    }
    
    
    
    
    
    
    
    
    /**
     * Inspect provided type and retrieve its structure.
     * @param type type to inspect.
     * @param target target bound to populate with data.
     * @param aliases cache of generic variable declarations accessible for given type.
     * @param cache cache of already parsed classes.
     * @return Cached or <code>target</code> bound.
     */
    private static Bound inspect(Type type, ReflectionBound target, Map<String, Bound> aliases, Map<Type, Bound> cache) {
        // Return immediately if type was already inspected and cached.
        if (cache.containsKey(type)) {
            return cache.get(type);
        }
        
        
        
        
        
        if (type instanceof Class) {
            target.type = type;
            // Building context-wide cache for class object.
            Map<Type, Bound> context = new HashMap<Type, Bound>(cache);
            context.put(type, target);
            
            Class<?> t = (Class) type;
            Map<String, Bound> classParams = new HashMap<String, Bound>();
            for (TypeVariable param : t.getTypeParameters()) {
                String name = param.getName();
                Bound paramBound = aliases.get(name);
                if (paramBound == null) {
                    paramBound = inspect(param, new ReflectionBound(), aliases, context);
                }
                classParams.put(name, paramBound);
                target.parameters.add(paramBound);
            }
            
            
            
            
            
            Type superType = t.getGenericSuperclass();
            if (superType != null) {
                target.parents.add(inspect(superType, new ReflectionBound(), classParams, context));
            }
            for (Type implemetedInterface : t.getGenericInterfaces()) {
                target.parents.add(inspect(implemetedInterface, new ReflectionBound(), classParams, context));
            }
            return target;
            
        }
        
        
        
        
        // <editor-fold name="A[]">
        if (type instanceof GenericArrayType) {
            target.type = Array.class;
            // Get type of array element, must not be empty.
            Type t = ((GenericArrayType) type).getGenericComponentType(); // A
            target.parameters.add(inspect(t, new ReflectionBound(), aliases, cache));
            return target;
        }
        // </editor-fold>
            
            
            
            
            
            
            
        // <editor-fold name="A extends B & C">
        if (type instanceof TypeVariable) {
            TypeVariable t = (TypeVariable) type;
            String name = t.getName(); // A
            // If this variable was already inspected then return.
            if (aliases.containsKey(name)) {
                return aliases.get(name);
            }
            Type[] bounds = t.getBounds(); // [B, C]
            if (bounds.length > 1) {
                target = createBound(type, target);
                for (Type bound : bounds) {
                    target.parents.add(inspect(bound, null, aliases, cache));
                }
                return target;
            } else {
                if (bounds.length == 0) {
                    // Any type may be provided to parameter with no upper bounds specified.
                    target = createBound(type, target);
                    target.type = Object.class;
                    return target;
                } else {
                    return inspect(bounds[0], null, aliases, cache);
                }
            }
        }
        // </editor-fold>
        
        // Inspect: ?, ? extends Number, ? super Integer, etc.
        // <editor-fold name="">
        // </editor-fold>
        if (type instanceof WildcardType) {
            WildcardType t = (WildcardType) type;
            Type[] bounds = t.getUpperBounds();
            if (bounds.length > 1) {
                target = createBound(type, target);
                for (Type bound : bounds) {
                    target.parents.add(inspect(bound, null, aliases, cache));
                }
                return target;
            } else {
                if (bounds.length == 0) {
                    // Any type may be provided to parameter with no upper bounds specified.
                    target = createBound(type, target);
                    target.type = Object.class;
                    return target;
                } else {
                    return inspect(bounds[0], null, aliases, cache);
                }
            }
        }
        
        // Inspect: AnotherClass<...>
        // <editor-fold name="">
        // </editor-fold>
        if (type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type;
            
            target = createBound(type, target);
            target.type = (Class) t.getRawType();
            
            Type[] parameters = t.getActualTypeArguments(); // Actual content of <...> class.
            TypeVariable[] naming = target.type.getTypeParameters(); // Names of class parameters from class definition.
            
            Map<String, Bound> locals = new HashMap<String, Bound>();
            
            // Inspect parameters of this parameterized class.
            // Array of actual parameters and their namings are parallel.
            for (int i = 0; i < parameters.length; i++) {
                locals.put(naming[i].getName(), inspect(parameters[i], null, aliases, cache));
            }
            return inspect(target.type, target, locals, cache);
        }
        
        throw new IllegalStateException();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Class<C> getType() {
        return (Class<C>) type;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public List<Bound<?>> getParameters() {
        return parameters;
    }

    @Override
    public List<Bound<?>> getInterfaces() {
        return parents;
    }

    @Override
    public <T> Bound<T> findImplementedBoundOfType(Class<T> type) {
        return findImplementedBoundOfType(type, new ArrayList<Bound<?>>());
    }

    private <T> Bound<T> findImplementedBoundOfType(Class<T> type, List<Bound<?>> ancestors) {
        for (Bound ancestor : ancestors) {
            if (!type.isAssignableFrom(ancestor.getType())) {
                continue;
            }
            Bound t = findImplementedBoundOfType(type, ancestor.getInterfaces());
            if (t instanceof ReflectionBound) {
                return t;
            }
            return ancestor;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        if (type != null) {
            out.append(type.getSimpleName());
        } else {
            out.append(rawType.getClass().getSimpleName());
        }
        if (!parameters.isEmpty()) {
            out.append('<').append(parameters).append('>');
        }
        if (!parents.isEmpty()) {
            out.append(" extends ").append(parents);
        }
        return out.toString();
    }
}