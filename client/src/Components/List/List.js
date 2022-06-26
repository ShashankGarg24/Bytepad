import Download from '../../assets/images/download.png';
import Share from '../../assets/images/share.png';
import styles from './List.module.css';

export default function List(props){

    return(

          <div className={styles.List}>

            <div className={styles.List_section1}>   
                Basic of electronics device
            </div>

            <div className={styles.List_section2}>
                <span>Question</span>
                <span>Odd sem</span>
                <span>Put</span>
                <span>2019-2020</span>
                <img src={Share} alt="share"/>
                <img src={Download} alt="download"/>
                
            </div>
        </div>
  
    )
}