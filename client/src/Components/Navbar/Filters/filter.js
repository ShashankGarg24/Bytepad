import { Menu, Dropdown } from "antd";
import { DownOutlined } from "@ant-design/icons";
import sty from "./filter.module.css";
import { connect } from 'react-redux';
import * as actionCreators from '../../../store/actions/actions';

function Filter(props) {

  const handleOptionClick = (item) => {
    console.log(item,props.heading);
    if(props.heading === 'Exam'){
      props.changeExamType(item);
    }
    else if(props.heading === 'Session'){
      props.changeSession(item);
    }
    else if(props.heading === 'Type'){
      props.changePaperType(item);
    }
  }

  const menu = (
    <Menu className={sty.Menu}>
      {props.options.map((item, index) => {
        return (
          <Menu.Item onClick={() => handleOptionClick(item)} key={index} className={sty.MenuItem}>
            {item}
          </Menu.Item>
        );
      })}
    </Menu>
  );

  return (
    <Dropdown className={sty.Dropdown} overlay={menu} trigger={["hover"]}>
      <div className={sty.Dropdown_filter}>
        <div className={sty.Filter}>
          <span>{props.heading}</span>
          <span>{props.default}</span>
        </div>
        <DownOutlined className={sty.DownOutlined} />
      </div>
    </Dropdown>
  );
}

const mapStateToProps = state => {
  return {
      stateData: state.filters
  }
};

const mapDispatchToProps = dispatch => {
  return {
      changeExamType: (type) => dispatch(actionCreators.changeExamType(type)),
      changeSession: (type) => dispatch(actionCreators.changeSession(type)),
      changePaperType: (type) => dispatch(actionCreators.changePaperType(type))
    }
};

export default connect(mapStateToProps,mapDispatchToProps)(Filter);

