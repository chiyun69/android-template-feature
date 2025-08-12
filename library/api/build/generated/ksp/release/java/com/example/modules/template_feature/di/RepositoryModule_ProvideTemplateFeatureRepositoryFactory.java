package com.example.modules.template_feature.di;

import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDao;
import com.example.modules.template_feature.data.localdatasource.preferences.TemplateFeaturePreferences;
import com.example.modules.template_feature.data.remotedatasource.api.TemplateFeatureApiService;
import com.example.modules.template_feature.domain.repositories.TemplateFeatureRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideTemplateFeatureRepositoryFactory implements Factory<TemplateFeatureRepository> {
  private final Provider<TemplateFeatureApiService> apiServiceProvider;

  private final Provider<TemplateFeatureDao> daoProvider;

  private final Provider<TemplateFeaturePreferences> preferencesProvider;

  public RepositoryModule_ProvideTemplateFeatureRepositoryFactory(
      Provider<TemplateFeatureApiService> apiServiceProvider,
      Provider<TemplateFeatureDao> daoProvider,
      Provider<TemplateFeaturePreferences> preferencesProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.daoProvider = daoProvider;
    this.preferencesProvider = preferencesProvider;
  }

  @Override
  public TemplateFeatureRepository get() {
    return provideTemplateFeatureRepository(apiServiceProvider.get(), daoProvider.get(), preferencesProvider.get());
  }

  public static RepositoryModule_ProvideTemplateFeatureRepositoryFactory create(
      Provider<TemplateFeatureApiService> apiServiceProvider,
      Provider<TemplateFeatureDao> daoProvider,
      Provider<TemplateFeaturePreferences> preferencesProvider) {
    return new RepositoryModule_ProvideTemplateFeatureRepositoryFactory(apiServiceProvider, daoProvider, preferencesProvider);
  }

  public static TemplateFeatureRepository provideTemplateFeatureRepository(
      TemplateFeatureApiService apiService, TemplateFeatureDao dao,
      TemplateFeaturePreferences preferences) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideTemplateFeatureRepository(apiService, dao, preferences));
  }
}
