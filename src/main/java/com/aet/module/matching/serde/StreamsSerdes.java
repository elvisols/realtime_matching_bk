package com.aet.module.matching.serde;

import com.aet.module.matching.entities.GLCore;
import com.aet.module.matching.entities.Journal;
import com.aet.module.matching.entities.Postilion;
import com.aet.module.matching.entities.TestOne;
import com.aet.module.matching.entities.TestTwo;
import com.aet.module.matching.entities.Tuple;
import com.aet.module.matching.serializer.JsonDeserializer;
import com.aet.module.matching.serializer.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.util.Map;


public class StreamsSerdes {

    public static Serde<TestTwo> TestTwoSerde() {
             return new TestTwoSerde();
    }
    
    public static Serde<TestOne> TestOneSerde() {
    	return new TestOneSerde();
    }
    
    public static Serde<Journal> JournalSerde() {
    	return new JournalSerde();
    }
    
    public static Serde<GLCore> GLCoreSerde() {
    	return new GLCoreSerde();
    }
    
    public static Serde<Postilion> PostilionSerde() {
    	return new PostilionSerde();
    }
    
    public static Serde<com.aet.module.matching.entities.core.Unimpacted> CoreUnimpactedItemSerde() {
    	return new CoreUnimpactedItemSerde();
    }
    
    public static Serde<com.aet.module.matching.entities.core.Matched> CoreMatchedItemSerde() {
    	return new CoreMatchedItemSerde();
    }
    
    public static Serde<com.aet.module.matching.entities.core.Dispense> CoreDispenseItemSerde() {
    	return new CoreDispenseItemSerde();
    }

    public static Serde<Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense>> CoreExceptionLogTupleSerde() {
        return new CoreExceptionLogTupleSerde();
    }
    
    public static Serde<com.aet.module.matching.entities.fep.Unimpacted> FepUnimpactedItemSerde() {
    	return new FepUnimpactedItemSerde();
    }
    
    public static Serde<com.aet.module.matching.entities.fep.Matched> FepMatchedItemSerde() {
    	return new FepMatchedItemSerde();
    }
    
    public static Serde<com.aet.module.matching.entities.fep.Dispense> FepDispenseItemSerde() {
    	return new FepDispenseItemSerde();
    }
    
    public static Serde<Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense>> FepExceptionLogTupleSerde() {
    	return new FepExceptionLogTupleSerde();
    }

    public static final class TestTwoSerde extends WrapperSerde<TestTwo> {
        public TestTwoSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>(TestTwo.class));
        }
    }
    
    public static final class TestOneSerde extends WrapperSerde<TestOne> {
    	public TestOneSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(TestOne.class));
    	}
    }    
    
    public static final class JournalSerde extends WrapperSerde<Journal> {
    	public JournalSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(Journal.class));
    	}
    }
    
    public static final class GLCoreSerde extends WrapperSerde<GLCore> {
    	public GLCoreSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(GLCore.class));
    	}
    }
    
    public static final class PostilionSerde extends WrapperSerde<Postilion> {
    	public PostilionSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(Postilion.class));
    	}
    }
    
    public static final class CoreUnimpactedItemSerde extends WrapperSerde<com.aet.module.matching.entities.core.Unimpacted> {
    	public CoreUnimpactedItemSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(com.aet.module.matching.entities.core.Unimpacted.class));
    	}
    }
    
    public static final class CoreMatchedItemSerde extends WrapperSerde<com.aet.module.matching.entities.core.Matched> {
    	public CoreMatchedItemSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(com.aet.module.matching.entities.core.Matched.class));
    	}
    }
    
    public static final class CoreDispenseItemSerde extends WrapperSerde<com.aet.module.matching.entities.core.Dispense> {
    	public CoreDispenseItemSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(com.aet.module.matching.entities.core.Dispense.class));
    	}
    }
    
    public static final class CoreExceptionLogTupleSerde extends WrapperSerde<Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense>> {
	        private static final Type tupleType = new TypeToken<Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense>>(){}.getType();
	    public CoreExceptionLogTupleSerde() {
	        super(new JsonSerializer<>(), new JsonDeserializer<>(tupleType));
	    }
	}
    
    public static final class FepUnimpactedItemSerde extends WrapperSerde<com.aet.module.matching.entities.fep.Unimpacted> {
    	public FepUnimpactedItemSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(com.aet.module.matching.entities.fep.Unimpacted.class));
    	}
    }
    
    public static final class FepMatchedItemSerde extends WrapperSerde<com.aet.module.matching.entities.fep.Matched> {
    	public FepMatchedItemSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(com.aet.module.matching.entities.fep.Matched.class));
    	}
    }
    
    public static final class FepDispenseItemSerde extends WrapperSerde<com.aet.module.matching.entities.fep.Dispense> {
    	public FepDispenseItemSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(com.aet.module.matching.entities.fep.Dispense.class));
    	}
    }
    
    public static final class FepExceptionLogTupleSerde extends WrapperSerde<Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense>> {
    	private static final Type tupleType = new TypeToken<Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense>>(){}.getType();
    	public FepExceptionLogTupleSerde() {
    		super(new JsonSerializer<>(), new JsonDeserializer<>(tupleType));
    	}
    }

    private static class WrapperSerde<T> implements Serde<T> {

        private JsonSerializer<T> serializer;
        private JsonDeserializer<T> deserializer;

         WrapperSerde(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public void configure(Map<String, ?> map, boolean b) {

        }

        @Override
        public void close() {

        }

        @Override
        public Serializer<T> serializer() {
           return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
           return deserializer;
        }
    }
}
