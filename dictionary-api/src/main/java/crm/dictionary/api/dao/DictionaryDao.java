package crm.dictionary.api.dao;

import crm.dao.NamedUniqEntity;
import crm.dao.NamedObjectDao;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DictionaryDao<T extends NamedUniqEntity> extends NamedObjectDao<T> {

    @NotNull Flux<T> findAll();

    @NotNull Mono<LongDictionary<T>> findAllAsMap();
}
