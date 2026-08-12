import { Outlet } from 'react-router-dom';
import Header from '../Features/shared/Header';
import Navigation from '../Features/shared/Navigation/Navigation';
import Footer from '../Features/shared/Footer';

function MainLayout({ tabs = [] }) {
  return (
    <>
      <div className="container py-4">
        <Header />

        {tabs ? <Navigation tabs={tabs} /> : <div></div>}

        <main className="py-2">
          <Outlet />
        </main>

        <Footer />
      </div>
    </>
  );
}

export default MainLayout;
