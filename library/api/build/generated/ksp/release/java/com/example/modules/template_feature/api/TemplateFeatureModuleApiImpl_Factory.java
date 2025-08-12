package com.example.modules.template_feature.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class TemplateFeatureModuleApiImpl_Factory implements Factory<TemplateFeatureModuleApiImpl> {
  @Override
  public TemplateFeatureModuleApiImpl get() {
    return newInstance();
  }

  public static TemplateFeatureModuleApiImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TemplateFeatureModuleApiImpl newInstance() {
    return new TemplateFeatureModuleApiImpl();
  }

  private static final class InstanceHolder {
    static final TemplateFeatureModuleApiImpl_Factory INSTANCE = new TemplateFeatureModuleApiImpl_Factory();
  }
}
