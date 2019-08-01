package entity.devicestatic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author gmq
 * @since 2019-05-22
 */

@Entity
@Table(name="device_static")
@DynamicInsert
@DynamicUpdate
public class DeviceStatic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dailyCount;
    private Long totalCount;
    private Long onlineCount;
    private Long statusCount;
    private Long activeCount;
 	@Temporal(value = TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

   public Long getDailyCount() {
      return dailyCount;
   }
   public void setDailyCount(Long dailyCount) {
      this.dailyCount = dailyCount;
   }
   public Long getTotalCount() {
      return totalCount;
   }
   public void setTotalCount(Long totalCount) {
      this.totalCount = totalCount;
   }
   public Long getOnlineCount() {
      return onlineCount;
   }
   public void setOnlineCount(Long onlineCount) {
      this.onlineCount = onlineCount;
   }
   public Long getStatusCount() {
      return statusCount;
   }
   public void setStatusCount(Long statusCount) {
      this.statusCount = statusCount;
   }
   public Long getActiveCount() {
      return activeCount;
   }
   public void setActiveCount(Long activeCount) {
      this.activeCount = activeCount;
   }
   public Date getDate() {
      return date;
   }
   public void setDate(Date date) {
      this.date = date;
   }


}