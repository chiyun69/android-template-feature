package com.example.modules.template_feature.di;

import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository;
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class UseCaseModule_ProvideSaveTemplateFeatureDataUseCaseFactory implements Factory<SaveTemplateFeatureDataUseCase> {
  private final Provider<TemplateFeatureRepository> repositoryProvider;

  public UseCaseModule_ProvideSaveTemplateFeatureDataUseCaseFactory(
      Provider<TemplateFeatureRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SaveTemplateFeatureDataUseCase get() {
    return provideSaveTemplateFeatureDataUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideSaveTemplateFeatureDataUseCaseFactory create(
      Provider<TemplateFeatureRepository> repositoryProvider) {
    return new UseCaseModule_ProvideSaveTemplateFeatureDataUseCaseFactory(repositoryProvider);
  }

  public static SaveTemplateFeatureDataUseCase provideSaveTemplateFeatureDataUseCase(
      TemplateFeatureRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideSaveTemplateFeatureDataUseCase(repository));
  }
}
