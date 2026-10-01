1. ReentrantLock
2. Random() в многопоточном приложении
   В документации указано: "_Instances of java.util.Random are threadsafe. However, the concurrent use of the same java.util.Random instance across threads may encounter contention and consequent poor performance. Consider instead using ThreadLocalRandom in multithreaded designs._"
   хотя random() потокобезопасен, метод использует операцию CAS для обновления исходного кода. В сценариях с высокой конкуренцией потоков эта операция CAS, скорее всего, завершится неудачей, что приведет к повторным попыткам, которые, в свою очередь, потребляют много ресурсов центрального процессора, что значительно снижает производительность.
source:
https://medium.com/@freeyecheng/are-you-still-using-random-to-generate-random-numbers-in-high-concurrency-scenarios-022f2dfe7bff