package com.example.modules.template_feature.di;

import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDao;
import com.example.modules.template_feature.data.localdatasource.database.TemplateFeatureDatabase;
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
public final class DatabaseModule_ProvideDaoFactory implements Factory<TemplateFeatureDao> {
  private final Provider<TemplateFeatureDatabase> databaseProvider;

  public DatabaseModule_ProvideDaoFactory(Provider<TemplateFeatureDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TemplateFeatureDao get() {
    return provideDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDaoFactory create(
      Provider<TemplateFeatureDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDaoFactory(databaseProvider);
  }

  public static TemplateFeatureDao provideDao(TemplateFeatureDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDao(database));
  }
}
