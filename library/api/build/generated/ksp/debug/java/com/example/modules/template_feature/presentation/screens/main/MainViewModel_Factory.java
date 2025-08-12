package com.example.modules.template_feature.presentation.screens.main;

import com.example.modules.template_feature.domain.usecases.GetTemplateFeatureDataUseCase;
import com.example.modules.template_feature.domain.usecases.SaveTemplateFeatureDataUseCase;
import com.example.modules.template_feature.domain.usecases.SyncTemplateFeatureUseCase;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<GetTemplateFeatureDataUseCase> getTemplateFeatureDataUseCaseProvider;

  private final Provider<SaveTemplateFeatureDataUseCase> saveTemplateFeatureDataUseCaseProvider;

  private final Provider<SyncTemplateFeatureUseCase> syncTemplateFeatureUseCaseProvider;

  public MainViewModel_Factory(
      Provider<GetTemplateFeatureDataUseCase> getTemplateFeatureDataUseCaseProvider,
      Provider<SaveTemplateFeatureDataUseCase> saveTemplateFeatureDataUseCaseProvider,
      Provider<SyncTemplateFeatureUseCase> syncTemplateFeatureUseCaseProvider) {
    this.getTemplateFeatureDataUseCaseProvider = getTemplateFeatureDataUseCaseProvider;
    this.saveTemplateFeatureDataUseCaseProvider = saveTemplateFeatureDataUseCaseProvider;
    this.syncTemplateFeatureUseCaseProvider = syncTemplateFeatureUseCaseProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(getTemplateFeatureDataUseCaseProvider.get(), saveTemplateFeatureDataUseCaseProvider.get(), syncTemplateFeatureUseCaseProvider.get());
  }

  public static MainViewModel_Factory create(
      Provider<GetTemplateFeatureDataUseCase> getTemplateFeatureDataUseCaseProvider,
      Provider<SaveTemplateFeatureDataUseCase> saveTemplateFeatureDataUseCaseProvider,
      Provider<SyncTemplateFeatureUseCase> syncTemplateFeatureUseCaseProvider) {
    return new MainViewModel_Factory(getTemplateFeatureDataUseCaseProvider, saveTemplateFeatureDataUseCaseProvider, syncTemplateFeatureUseCaseProvider);
  }

  public static MainViewModel newInstance(
      GetTemplateFeatureDataUseCase getTemplateFeatureDataUseCase,
      SaveTemplateFeatureDataUseCase saveTemplateFeatureDataUseCase,
      SyncTemplateFeatureUseCase syncTemplateFeatureUseCase) {
    return new MainViewModel(getTemplateFeatureDataUseCase, saveTemplateFeatureDataUseCase, syncTemplateFeatureUseCase);
  }
}
