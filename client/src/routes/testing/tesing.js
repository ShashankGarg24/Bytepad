import React from 'react';
import List from '../../Components/List/List';
import { Pagination } from 'antd';

export default function Test(props){

    const array = [{name:"ayush"},{name:'aditiya'},{name:'ed'},{name:"rik"},{name:'some'},{name:'abhay'},{name:'manish'},{name:"ayush"},{name:'aditiya'},{name:'ed'}];

    var item_per_page= 3;
    const [pagination,setPagination] = React.useState({min:0,max:item_per_page})

    const handleChange = (value,number)=>{
      
        let min = ( (value-1)*item_per_page ) +1
        let max= min+item_per_page;
        setPagination({min:min,max:max})
        console.log('on change triggered')
    }


    console.log("min===",pagination["min"])
    console.log('max===',pagination['max']);
    return(

        <div style={{marginLeft:"50px",marginTop:"50px"}}>

                {array.slice(pagination['min'],pagination['max']).map((element,index)=>(
                    // <List/>
                    <h6>{element.name}</h6>
                ))}
              
              <Pagination
                    total={12}
                    responsive={true}
                    showSizeChanger
                    showQuickJumper
                    defaultPageSize={item_per_page}
                    showTotal={total => `Total ${total} items`}
                    onChange={(value,number)=> handleChange(value,number)}
                    />
        </div>         
        
  
    )
}