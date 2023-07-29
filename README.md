<!--
Hey, thanks for using the awesome-readme-template template.  
If you have any enhancements, then fork this project and create a pull request 
or just open an issue with the label "enhancement".

Don't forget to give this project a star for additional support ;)
Maybe you can mention me or this repo in the acknowledgements too
-->

<!--
This README is a slimmed down version of the original one.
Removed sections:
- Screenshots
- Running Test
- Deployment
- FAQ
- Acknowledgements
-->

<div align="center">

  <img src="assets/logo.png" alt="logo" width="200" height="auto" />
  <h1>Project Database App README</h1>
  
  <p>
    Menu driven database app for storing home projects. 
  </p>

  
<!-- Badges -->
<!--
<p>
  <a href="https://github.com/Louis3797/awesome-readme-template/graphs/contributors">
    <img src="https://img.shields.io/github/contributors/Louis3797/awesome-readme-template" alt="contributors" />
  </a>
  <a href="">
    <img src="https://img.shields.io/github/last-commit/Louis3797/awesome-readme-template" alt="last update" />
  </a>
  <a href="https://github.com/Louis3797/awesome-readme-template/network/members">
    <img src="https://img.shields.io/github/forks/Louis3797/awesome-readme-template" alt="forks" />
  </a>
  <a href="https://github.com/Louis3797/awesome-readme-template/stargazers">
    <img src="https://img.shields.io/github/stars/Louis3797/awesome-readme-template" alt="stars" />
  </a>
  <a href="https://github.com/Louis3797/awesome-readme-template/issues/">
    <img src="https://img.shields.io/github/issues/Louis3797/awesome-readme-template" alt="open issues" />
  </a>
  <a href="https://github.com/Louis3797/awesome-readme-template/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/Louis3797/awesome-readme-template.svg" alt="license" />
  </a>
</p>
-->

<!--
<h4>
    <a href="https://github.com/Louis3797/awesome-readme-template/">View Demo</a>
  <span> · </span>
    <a href="https://github.com/Louis3797/awesome-readme-template">Documentation</a>
  <span> · </span>
    <a href="https://github.com/Louis3797/awesome-readme-template/issues/">Report Bug</a>
  <span> · </span>
    <a href="https://github.com/Louis3797/awesome-readme-template/issues/">Request Feature</a>
  </h4>
-->
</div>

<br />

<!-- Table of Contents -->
# Table of Contents

- [About the Project](#about-the-project)
  * [Tech Stack](#tech-stack)
  * [Features](#features)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)
  

<!-- About the Project -->
## About the Project


<!-- TechStack -->
### Tech Stack

<details>
  <summary>Client</summary>
  <ul>
    <li><a href="https://www.java.com/en/">Java</a></li>
  </ul>
</details>

<details>
  <summary>Server</summary>
  <ul>
    <li><a href="https://dbeaver.io/">DBeaver</a></li>
  </ul>
</details>

<details>
<summary>Database</summary>
  <ul>
   <li><a href="https://www.mysql.com/">MySQL</a></li>
  </ul>
</details>

<!-- Features -->
### Features

- Create, Read, Update, and Delete Projects from Database
- Interactive Menu


<!-- Usage -->
## Usage

Use this app to store all of your project ideas!


```java
public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project.",
			"2) List projects.",
			"3) Select a project.",
			"4) Update project details.",
			"5) Delete a project."
	);
	// @formatter:on
```

<!-- Contributing -->
## Contributing

Contributions are always welcome!


<!-- License -->
## License

Distributed under the no License.

<!-- Contact -->
## Contact

Clay Rehmel - clay.rehmel@gmail.com - [LinkedIn](https://www.linkedin.com/in/clay-rehmel-07b8b4214/)

Project Link: [https://github.com/rehmel23/Project-Database-App/](https://github.com/rehmel23/Project-Database-App/)

<!-- Acknowledgments -->
## Acknowledgements

 - [Awesome README](https://github.com/matiassingers/awesome-readme)
 - [Readme Template](https://github.com/othneildrew/Best-README-Template)
