/*
 * ┌──┐
 * │  │
 * │Eh│ony
 * └──┘
 */
package org.ehony.pojoghost.core.impl;

import java.lang.reflect.*;
import java.util.*;
import org.ehony.pojoghost.core.Bound;

public class BasicBound<T> implements Bound<T> {

    public static BasicBound inspect(Type type) {
        if (type == null) {
            throw new NullPointerException("Type expected.");
        }
        return inspect(type, new HashMap(), new HashMap());
    }

    private static <T> BasicBound<T> inspect(Type type, Map<String, BasicBound> predefinedVars, Map<Class, BasicBound> parents) {
        if (type instanceof Class) {
            Class c = (Class)type;
            if (parents.containsKey(c)) {
                return parents.get(c);
            }
            BasicBound tree = new BasicBound(c);

            Map<Class, BasicBound> myParents = new HashMap(parents);
            myParents.put(c, tree);

            Map<String, BasicBound> classVars = new HashMap();
            for (TypeVariable param : c.getTypeParameters()) {
                BasicBound var = predefinedVars.get(param.getName());
                if (var == null) {
                    var = inspect(param, predefinedVars, myParents);
                }
                classVars.put(param.getName(), var);
                tree.getParameterBounds().add(var);
            }
            Type myClass = c.getGenericSuperclass();
            if (myClass instanceof Type) {
                tree.getImplemetedBounds().add(inspect(myClass, classVars, myParents));
            }
            for (Type myInterface : c.getGenericInterfaces()) {
                tree.getImplemetedBounds().add(inspect(myInterface, classVars, myParents));
            }
            return tree;
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType)type;
            List<BasicBound> vars = new ArrayList();
            for (Type param : p.getActualTypeArguments()) {
                vars.add(inspect(param, predefinedVars, parents));
            }
            Class c = (Class)p.getRawType();
            Map<String, BasicBound> classVars = new HashMap();
            TypeVariable[] remoteVars = c.getTypeParameters();
            for (int i = 0; i < vars.size(); i++) {
                classVars.put(remoteVars[i].getName(), vars.get(i));
            }
            return inspect(c, classVars, parents);
        }

        if (type instanceof TypeVariable) {
            TypeVariable v = (TypeVariable)type;
            if (predefinedVars.containsKey(v.getName())) {
                return predefinedVars.get(v.getName());
            }
            if (v.getBounds().length > 1) {
                BasicBound tree = new BasicBound(null);
                for (Type t : v.getBounds()) {
                    tree.getImplemetedBounds().add(inspect(t, predefinedVars, parents));
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
            Type[] bounds = ((WildcardType)type).getUpperBounds();
            if (bounds.length > 1) {
                BasicBound tree = new BasicBound(null);
                for (Type t : bounds) {
                    tree.getImplemetedBounds().add(inspect(t, predefinedVars, parents));
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
            BasicBound tree = inspect(Array.class);
            tree.getParameterBounds().add(inspect(((GenericArrayType)type).getGenericComponentType(), predefinedVars, parents));
            return tree;
        }

        throw new IllegalStateException();
    }
    private Class<T> type;
    private List<Bound> generics = new ArrayList(),
            ancestors = new ArrayList();

    public BasicBound(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    public Type getRawType() {
        return null;
    }

    public <B> Bound<B> findImplemetedBoundOfType(Class<B> type) {
        return findImplemetedBoundOfType(type, new ArrayList());
    }

    private <B> Bound<B> findImplemetedBoundOfType(Class<B> type, List<Bound> ancestors) {
        ancestor:
        for (Bound ancestor : ancestors) {
            if (!type.isAssignableFrom(ancestor.getType())) {
                continue ancestor;
            }
            Bound tree = findImplemetedBoundOfType(type, ancestor.getImplemetedBounds());
            if (tree instanceof BasicBound) {
                return tree;
            }
            return ancestor;
        }
        return null;
    }

    public List<Bound> getImplemetedBounds() {
        return ancestors;
    }

    public List<Bound> getParameterBounds() {
        return generics;
    }

    @Override
    public String toString() {//this
        return toString(new ArrayList());
    }

    private String toString(List<Class> parents) {
        StringBuilder out;
        if (type != null) {
            out = new StringBuilder(type.getCanonicalName());
        } else {
            out = new StringBuilder("?");
        }
        if (!generics.isEmpty()) {
            out.append("<\n");
            for (int i = 0; i < generics.size(); i++) {
                BasicBound generic = (BasicBound)generics.get(i);
                if (!parents.contains(generic.getType())) {
                    List<Class> myParents = new ArrayList<Class>(parents);
                    if (generic.getType() != null) {
                        myParents.add(generic.getType());
                    }
                    out.append(generic.toString(myParents).replaceAll("^|(\\n)", "$1\t"));
                } else {
                    out.append("\t" + generic.getType().getCanonicalName() + "[...]");
                }
                if (i < generics.size() - 1) {
                    out.append(",\n");
                }
            }
            out.append("\n>");
        }
        if (!ancestors.isEmpty()) {
            out.append(" extends {\n");
            for (int i = 0; i < ancestors.size(); i++) {
                BasicBound ancestor = (BasicBound)ancestors.get(i);
                if (!parents.contains(ancestor.getType())) {
                    List<Class> myParents = new ArrayList<Class>(parents);
                    if (ancestor.getType() != null) {
                        myParents.add(ancestor.getType());
                    }
                    out.append(ancestor.toString(myParents).replaceAll("^|(\\n)", "$1\t"));
                } else {
                    out.append("\t" + ancestor.getType().getCanonicalName() + "[...]");
                }
                if (i < ancestors.size() - 1) {
                    out.append(",\n");
                }
            }
            out.append("\n}");
        }
        return out.toString();
    }
}