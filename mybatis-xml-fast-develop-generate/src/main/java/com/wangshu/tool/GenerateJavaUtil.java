package com.wangshu.tool;

import com.squareup.javapoet.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * @author wangshu-g
 */
public class GenerateJavaUtil {

    public GenerateJavaUtil() {
    }

    public static String getJavaCode(String packageName, TypeSpec typeSpec) throws IOException {
        JavaFile.Builder javaFileBuilder = GenerateJavaUtil.generateJavaFileBuilder(packageName, typeSpec);
        JavaFile javaFile = javaFileBuilder.build();
        JavaFileObject javaFileObject = javaFile.toJavaFileObject();
        return String.valueOf(javaFileObject.getCharContent(true));
    }

    @Contract("_, _ -> new")
    public static @NotNull JavaFile.Builder generateJavaFileBuilder(String packageName, TypeSpec typeSpec) {
        return JavaFile.builder(packageName, typeSpec);
    }

    @Contract("_, _ -> new")
    public static @NotNull JavaFile generateJavaFile(String packageName, TypeSpec typeSpec) {
        return generateJavaFileBuilder(packageName, typeSpec).build();
    }

    public static void writeJavaFile(String packageName, TypeSpec typeSpec, Filer filer) throws IOException {
        generateJavaFile(packageName, typeSpec).writeTo(filer);
    }

    public static @NotNull FieldSpec.Builder generateFieldBuilder(TypeName typeName, String name, Modifier modifier) {
        return FieldSpec.builder(typeName, name, modifier);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull FieldSpec generateField(TypeName typeName, String name, Modifier modifier) {
        return generateFieldBuilder(typeName, name, modifier).build();
    }

    @Contract("_ -> new")
    public static @NotNull AnnotationSpec.Builder generateAnnotationBuilder(Class<?> clazz) {
        return AnnotationSpec.builder(clazz);
    }

    @Contract("_ -> new")
    public static @NotNull AnnotationSpec generateAnnotationSpec(Class<?> clazz) {
        return AnnotationSpec.builder(clazz).build();
    }

    public static @NotNull MethodSpec.Builder generateMethodBuilder(String methodName, TypeName returnType, Class<?> anoClazz, Modifier modifier) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName).returns(returnType).addAnnotation(anoClazz);
        if (modifier != null) {
            methodBuilder.addModifiers(modifier);
        }
        return methodBuilder;
    }

    public static @NotNull ParameterSpec generateMethodParam(TypeName type, String name, Modifier modifier) {
        return ParameterSpec.builder(type, name, modifier).build();
    }

}
