package org.apache.spark.ml.parity.feature

import org.apache.spark.ml.bundle.SparkBundleContext
import org.apache.spark.ml.parity.SparkParityBase
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer}
import org.apache.spark.ml.{Pipeline, Transformer}
import org.apache.spark.sql.DataFrame

/**
  * Created by hollinwilkins on 10/30/16.
  */

class MIOOneHotEncoderParitySpec extends SparkParityBase {
  override val dataset: DataFrame = baseDataset.select("state")
  override val sparkTransformer: Transformer = new Pipeline()
      .setStages(Array(
        new StringIndexer().setInputCol("state").setOutputCol("state_index"),
        new OneHotEncoder().setInputCol("state_index").setOutputCol("state_oh")
      )).fit(dataset)
  override val unserializedParams: Set[String] = Set("stringOrderType")
}

class OneHotEncoderParitySpec extends SparkParityBase {
  override val dataset: DataFrame = baseDataset.select("state")
  override val sparkTransformer: Transformer =
      new Pipeline()
        .setStages(Array(
          new StringIndexer().setInputCol("state").setOutputCol("state_index"),
          new StringIndexer().setInputCol("state").setOutputCol("state_index2"),
          new OneHotEncoder()
            .setInputCols(Array("state_index", "state_index2"))
            .setOutputCols(Array("state_oh", "state_oh2"))
        ))
        .fit(dataset)

  override val unserializedParams = Set("stringOrderType")



  it("fails to instantiate if the Spark model sets inputCol and inputCols"){
    intercept[IllegalArgumentException] {
      new OneHotEncoder()
        .setInputCol("state")
        .setInputCols(Array("state_index", "state_index2"))
        .setOutputCols(Array("state_oh", "state_oh2"))
        .fit(dataset)
    }
  }

  it("fails to instantiate if the Spark model sets outputCol and outputCols"){
    intercept[IllegalArgumentException] {
      new OneHotEncoder()
        .setInputCol("state")
        .setOutputCol("state_oh")
        .setOutputCols(Array("state_oh", "state_oh2"))
        .fit(dataset)
    }
  }
}
