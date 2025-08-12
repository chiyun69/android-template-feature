package com.example.modules.template_feature.presentation.screens.detail;

import com.example.modules.template_feature.domain.usecases.GetTemplateFeatureDataUseCase;
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase;
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<GetTemplateFeatureDataUseCase> getTemplateFeatureDataUseCaseProvider;

  private final Provider<SaveTemplateFeatureDataUseCase> saveTemplateFeatureDataUseCaseProvider;

  public DetailViewModel_Factory(
      Provider<GetTemplateFeatureDataUseCase> getTemplateFeatureDataUseCaseProvider,
      Provider<SaveTemplateFeatureDataUseCase> saveTemplateFeatureDataUseCaseProvider) {
    this.getTemplateFeatureDataUseCaseProvider = getTemplateFeatureDataUseCaseProvider;
    this.saveTemplateFeatureDataUseCaseProvider = saveTemplateFeatureDataUseCaseProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(getTemplateFeatureDataUseCaseProvider.get(), saveTemplateFeatureDataUseCaseProvider.get());
  }

  public static DetailViewModel_Factory create(
      Provider<GetTemplateFeatureDataUseCase> getTemplateFeatureDataUseCaseProvider,
      Provider<SaveTemplateFeatureDataUseCase> saveTemplateFeatureDataUseCaseProvider) {
    return new DetailViewModel_Factory(getTemplateFeatureDataUseCaseProvider, saveTemplateFeatureDataUseCaseProvider);
  }

  public static DetailViewModel newInstance(
      GetTemplateFeatureDataUseCase getTemplateFeatureDataUseCase,
      SaveTemplateFeatureDataUseCase saveTemplateFeatureDataUseCase) {
    return new DetailViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase);
  }
}
