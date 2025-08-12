package com.example.modules.template_feature.di;

import com.example.modules.template_feature.data.remotedatasource.api.TemplateFeatureApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideApiServiceFactory implements Factory<TemplateFeatureApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public TemplateFeatureApiService get() {
    return provideApiService(retrofitProvider.get());
  }

  public static NetworkModule_ProvideApiServiceFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideApiServiceFactory(retrofitProvider);
  }

  public static TemplateFeatureApiService provideApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideApiService(retrofit));
  }
}
