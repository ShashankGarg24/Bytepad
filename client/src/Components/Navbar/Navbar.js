import React, { Component } from "react";
import { connect } from "react-redux";
import styles from "./Navbar.module.css";
import sty from "./filterNavbar.module.css";
import logo from "../../assets/images/logo.png";
import Filter from "./Filters/filter";
import "antd/dist/antd.css";

const examType = ["All Exams", "ST-1", "ST-2", "PUT", "UT"];
const examYears = [
  "All Years",
  "2020-2019",
  "2019-2018",
  "2018-2017",
  "2017-2016",
];
const paperType = ["All Papers", "Questions", "Answers"];

class Navbar extends Component {
  render() {
    return (
      <>
        {this.props.showFilters ? (
          <nav className={sty.Filter_navbar}>
            <img className={sty.logo_image} src={logo} alt="logo" />
            <Filter
              heading={"Exam"}
              default={this.props.filters.examType}
              options={examType}
            />
            <Filter
              heading={"Session"}
              default={this.props.filters.examYear}
              options={examYears}
            />
            <Filter
              heading={"Type"}
              default={this.props.filters.paperType}
              options={paperType}
            />
          </nav>
        ) : (
          <nav className={styles.navbar}>
            <span className={styles.logo}>
              <img className={styles.logo_image} src={logo} alt="logo" />
              Bytepad
            </span>

            <div className={styles.navbarHeading}>
              <span>Mobile app</span>
            </div>
          </nav>
        )}
      </>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    filters: state.filter,
  };
};

export default connect(mapStateToProps)(Navbar);
