package com.example.modules.template_feature.di;

import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository;
import com.example.modules.template_feature.domain.usecases.SyncTemplateFeatureUseCase;
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
public final class UseCaseModule_ProvideSyncTemplateFeatureUseCaseFactory implements Factory<SyncTemplateFeatureUseCase> {
  private final Provider<TemplateFeatureRepository> repositoryProvider;

  public UseCaseModule_ProvideSyncTemplateFeatureUseCaseFactory(
      Provider<TemplateFeatureRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SyncTemplateFeatureUseCase get() {
    return provideSyncTemplateFeatureUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideSyncTemplateFeatureUseCaseFactory create(
      Provider<TemplateFeatureRepository> repositoryProvider) {
    return new UseCaseModule_ProvideSyncTemplateFeatureUseCaseFactory(repositoryProvider);
  }

  public static SyncTemplateFeatureUseCase provideSyncTemplateFeatureUseCase(
      TemplateFeatureRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideSyncTemplateFeatureUseCase(repository));
  }
}
