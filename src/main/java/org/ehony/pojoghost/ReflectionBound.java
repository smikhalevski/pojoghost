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

    private static <T> Bound<T> inspect(Type type, Map<String, Bound> predefinedVars, Map<Class, Bound> parents) {
        
        
        // Interpreting: AnotherClass<Generic>
        if (type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type;
            Class c = (Class) t.getRawType();
            
            Type[] args = t.getActualTypeArguments();
            
            Map<String, Bound> classVars = new HashMap();
            TypeVariable[] remoteVars = c.getTypeParameters();
            for (int i = 0; i < args.length; i++) {
                classVars.put(remoteVars[i].getName(), inspect(args[i], predefinedVars, parents));
            }
            return inspect(c, classVars, parents);
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        if (type instanceof Class) {
            Class c = (Class) type;
            if (parents.containsKey(c)) {
                return parents.get(c);
            }
            Bound tree = new ReflectionBound(c);

            Map<Class, Bound> myParents = new HashMap(parents);
            myParents.put(c, tree);

            Map<String, Bound> classVars = new HashMap();
            for (TypeVariable param : c.getTypeParameters()) {
                Bound var = predefinedVars.get(param.getName());
                if (var == null) {
                    var = inspect(param, predefinedVars, myParents);
                }
                classVars.put(param.getName(), var);
                tree.getBoundsOfGenericParameters().add(var);
            }
            Type myClass = c.getGenericSuperclass();
            if (myClass instanceof Type) {
                tree.getImplementedBounds().add(inspect(myClass, classVars, myParents));
            }
            for (Type myInterface : c.getGenericInterfaces()) {
                tree.getImplementedBounds().add(inspect(myInterface, classVars, myParents));
            }
            return tree;
        }



        if (type instanceof TypeVariable) {
            TypeVariable v = (TypeVariable) type;
            if (predefinedVars.containsKey(v.getName())) {
                return predefinedVars.get(v.getName());
            }
            if (v.getBounds().length > 1) {
                Bound tree = new ReflectionBound(null);
                for (Type t : v.getBounds()) {
                    tree.getImplementedBounds().add(inspect(t, predefinedVars, parents));
                }
                return tree;
            } else {
                Type t = Object.class;
                if (v.getBounds().length > 0) {
                    t = v.getBounds()[0];
                }
                return inspect(t, predefinedVars, parents);
            }
        }

        if (type instanceof WildcardType) {
            Type[] bounds = ((WildcardType) type).getUpperBounds();
            if (bounds.length > 1) {
                Bound tree = new ReflectionBound(null);
                for (Type t : bounds) {
                    tree.getImplementedBounds().add(inspect(t, predefinedVars, parents));
                }
                return tree;
            } else {
                Type t = Object.class;
                if (bounds.length > 0) {
                    t = bounds[0];
                }
                return inspect(t, predefinedVars, parents);
            }
        }

        if (type instanceof GenericArrayType) {
            Bound tree = inspect(Array.class);
            tree.getBoundsOfGenericParameters()
                .add(inspect(((GenericArrayType) type).getGenericComponentType(), predefinedVars, parents));
            return tree;
        }

        throw new IllegalStateException();
    }


    @Override
    public Class<C> getType() {
        return type;
    }

    @Override
    public Type getRawType() {
        return null;
    }

    @Override
    public List<Bound> getBoundsOfGenericParameters() {
        return null;
    }

    @Override
    public List<Bound> getImplementedBounds() {
        return null;
    }

    @Override
    public <T> Bound<T> findImplementedBoundOfType(Class<T> type) {
        return null;
    }
}