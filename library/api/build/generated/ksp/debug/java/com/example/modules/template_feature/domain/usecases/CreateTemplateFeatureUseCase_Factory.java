package com.example.modules.template_feature.domain.usecases;

import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class CreateTemplateFeatureUseCase_Factory implements Factory<CreateTemplateFeatureUseCase> {
  private final Provider<TemplateFeatureRepository> repositoryProvider;

  public CreateTemplateFeatureUseCase_Factory(
      Provider<TemplateFeatureRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateTemplateFeatureUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CreateTemplateFeatureUseCase_Factory create(
      Provider<TemplateFeatureRepository> repositoryProvider) {
    return new CreateTemplateFeatureUseCase_Factory(repositoryProvider);
  }

  public static CreateTemplateFeatureUseCase newInstance(TemplateFeatureRepository repository) {
    return new CreateTemplateFeatureUseCase(repository);
  }
}
