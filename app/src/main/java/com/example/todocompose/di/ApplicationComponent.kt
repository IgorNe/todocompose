




@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun getBleRepository(): BleRepository
    fun getEthalonRepository(): EthalonRepository
    fun getDataBaseRepository():DataBaseRepository

}


@Module
class AppModule(private val application: Application){
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    @Singleton
    fun provideBleRepository(context: Context): BleRepository {
        return BleScannerRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideEthalonRepository(): EthalonRepository {
        return EthalonRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideDataBaseRepository(context: Context): DataBaseRepository {
        return DataBaseRepositoryImpl(context)
    }






    /*@Provides
    fun provideComputer(
        processor: Processor,
        ram: RAM,
        motherBoard: MotherBoard
    ): Computer {
        return  Computer(
            processor = processor,
            ram = ram,
            motherBoard = motherBoard
        )

    }
    @Provides
    fun provideProcessor(): Processor{
        return Processor()
    }

    @Provides
    fun provideRam(): RAM{
        return RAM()
    }

    @Provides
    fun provideMotherboard():MotherBoard{
        return MotherBoard()
    }*/
}