package com.example.modules.template_feature.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class TemplateFeatureModuleBuilder_Factory implements Factory<TemplateFeatureModuleBuilder> {
  private final Provider<TemplateFeatureModuleApi> apiImplProvider;

  public TemplateFeatureModuleBuilder_Factory(Provider<TemplateFeatureModuleApi> apiImplProvider) {
    this.apiImplProvider = apiImplProvider;
  }

  @Override
  public TemplateFeatureModuleBuilder get() {
    return newInstance(apiImplProvider.get());
  }

  public static TemplateFeatureModuleBuilder_Factory create(
      Provider<TemplateFeatureModuleApi> apiImplProvider) {
    return new TemplateFeatureModuleBuilder_Factory(apiImplProvider);
  }

  public static TemplateFeatureModuleBuilder newInstance(TemplateFeatureModuleApi apiImpl) {
    return new TemplateFeatureModuleBuilder(apiImpl);
  }
}
