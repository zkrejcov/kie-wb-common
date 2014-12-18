/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.services.datamodeller.core.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kie.workbench.common.services.datamodeller.core.JavaInterface;
import org.kie.workbench.common.services.datamodeller.core.JavaType;

public class JavaInterfaceImpl extends AbstractHasAnnotations implements JavaInterface  {

    protected int modifiers;

    protected String name;

    protected String packageName;

    protected String superClassName;

    protected List<String> interfaces = new ArrayList<String>(  );

    protected List<JavaType> nestedTypes = new ArrayList<JavaType>(  );

    protected JavaType enclosingType = null;

    public JavaInterfaceImpl(String packageName, String name, int modifiers, JavaType enclosingType) {
        this.setName(name);
        this.packageName = packageName;
        this.modifiers = modifiers;
        this.enclosingType = enclosingType;
    }

    public JavaInterfaceImpl(String packageName, String name, int modifiers) {
        this(packageName, name, modifiers, null);
    }

    public JavaInterfaceImpl(String packageName, String name, JavaType enclosingType) {
        this(packageName, name, Modifier.PUBLIC, enclosingType);
    }

    public JavaInterfaceImpl(String packageName, String name) {
        this(packageName, name, Modifier.PUBLIC);
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract( modifiers );
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return true;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }

    @Override
    public JavaType getEnclosingType() {
        return enclosingType;
    }

    @Override
    public String getClassName() {
        return ( (packageName != null && !"".equals(packageName)) ? packageName+"." : "") + getName();
    }

    @Override
    public List<String> getInterfaces() {
        return interfaces;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public List<JavaType> getNestedTypes() {
        return nestedTypes;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void setPackageName( String packageName ) {
        this.packageName = packageName;
    }

    @Override
    public boolean isPackagePrivate() {
        return !isPrivate() && !isProtected() && !isPublic();
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic( modifiers );
    }

    @Override
    public boolean isPrivate() {
        return Modifier.isPrivate( modifiers );
    }

    @Override
    public boolean isProtected() {
        return Modifier.isProtected( modifiers );
    }
}