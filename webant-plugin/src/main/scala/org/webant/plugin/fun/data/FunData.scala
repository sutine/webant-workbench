package org.webant.plugin.fun.data

import java.util.Date

import org.webant.commons.annotation.{Index, Type}
import org.webant.commons.entity.HttpDataEntity

import scala.beans.BeanProperty

@Index("webant")
@Type("data")
class FunData extends HttpDataEntity {
  @BeanProperty
  var userName: String = _
  @BeanProperty
  var avatarUrl: String = _
  @BeanProperty
  var profileUrl: String = _
  @BeanProperty
  var title: String = _
  @BeanProperty
  var content: String = _
  @BeanProperty
  var publishTime: Date = _
  @BeanProperty
  var imgUrl: String = _
  @BeanProperty
  var imgWith: Integer = _
  @BeanProperty
  var imgHeight: Integer = _
  @BeanProperty
  var videoUrl: String = _
  @BeanProperty
  var videoCaptureUrl: String = _
  @BeanProperty
  var videoDuration: String = _
  @BeanProperty
  var likeNum: Integer = _
  @BeanProperty
  var hateNum: Integer = _
  @BeanProperty
  var commentNum: Integer = _
  @BeanProperty
  var shareNum: Integer = _
  @BeanProperty
  var rewardNum: Integer = _
  @BeanProperty
  var funType: String = _
}
