/*
 * java/fesql-spark/src/main/scala/com/_4paradigm/fesql/spark/nodes/RenamePlan.scala
 * Copyright 2021 4Paradigm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com._4paradigm.fesql.spark.nodes

import com._4paradigm.fesql.spark._
import com._4paradigm.fesql.vm.{PhysicalRenameNode}

object RenamePlan {

  def gen(ctx: PlanContext, node: PhysicalRenameNode, input: SparkInstance): SparkInstance = {
    // Return the same dataframe for child node
    val outputDf = input.getDfConsideringIndex(ctx, node.GetNodeId())

    SparkInstance.createConsideringIndex(ctx, node.GetNodeId(), outputDf)
  }

}
